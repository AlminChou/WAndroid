package com.almin.arch.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.repository.Resource
import com.almin.arch.repository.Status
import com.almin.arch.viewmodel.Contract.PageEvent
import com.almin.arch.viewmodel.Contract.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2022-01-05.
 */
abstract class AbstractViewModel<S: PageState, E: PageEvent>(private val middleWareProvider: MiddleWareProvider?) : ViewModel() {

//    protected val initialState: S by lazy { createInitialState() }
    protected abstract fun initialState(): S

    val currentState: S
        get() = uiState.value

    private val _uiState : MutableStateFlow<S> = MutableStateFlow(initialState())
    val uiState = _uiState.asStateFlow()

    private val _event : MutableSharedFlow<E> = MutableSharedFlow()
    val event = _event.asSharedFlow()


    fun setEvent(event : E) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(action: S.() -> S) {
        _uiState.value = currentState.action()
    }

    abstract fun attach(arguments: Bundle?)

    protected abstract fun handleEvent(event: E)

    init {
        // subscribeEvents
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    fun <Model> api(networkBound: Flow<Resource<Model>>, block: ApiCallback<Model>.()-> Unit) : Job {
        val apiCallback = ApiCallback<Model>()
        block.invoke(apiCallback)
        return viewModelScope.launch {
            networkBound.collect{
                when(it.status){
                    Status.Loading -> apiCallback.prepare?.invoke()
                    Status.Success -> it.data?.let { value -> apiCallback.success?.invoke(value) }
                    Status.Cache -> {
                        it.cache?.let { value -> apiCallback.cache?.invoke(value) }
                    }
                    Status.Error -> {
                        it.throwable?.run {
                            middleWareProvider?.exceptionHandlerProvider()?.handle(this)
                            apiCallback.failed?.invoke(this)
                        }
                    }
                }
            }
        }
    }

    fun <Model> api(block: ApiCallback<Model>.()-> Unit) : Job {
        val apiCallback = ApiCallback<Model>()
        block.invoke(apiCallback)
        return viewModelScope.launch {
            flow {
                emit(apiCallback.call.invoke())
            }.onStart {
                apiCallback.prepare?.invoke()
            }.apiCatch {
                apiCallback.failed?.invoke(it)
            }.collect{
                apiCallback.success?.invoke(it)
            }
        }
    }

    fun <Model> cacheApi(block: ApiCallback<Model>.()-> Unit) : Job {
        val apiCallback = ApiCallback<Model>()
        block.invoke(apiCallback)
        return viewModelScope.launch {
            flow {
                emit(apiCallback.call.invoke())
            }.flowOn(Dispatchers.IO).onStart {
                apiCallback.prepare?.invoke()
            }.apiCatch {
                apiCallback.failed?.invoke(it)
            }.collect{
                apiCallback.success?.invoke(it)
            }
        }
    }

    fun <T> Flow<T>.apiCatch(action: suspend kotlinx.coroutines.flow.FlowCollector<T>.(cause: Throwable) -> Unit) : Flow<T> {
        return this.catch {
            middleWareProvider?.exceptionHandlerProvider()?.handle(it)
            action(it)
        }
    }

    class ApiCallback<Model>{
        internal var prepare: (() -> Unit)? = null
        internal lateinit var call: suspend (() -> Model)
        internal var success: ((model: Model) -> Unit)? = null
        internal var cache: ((model: Model) -> Unit)? = null
        internal var failed: ((error: Throwable) -> Unit)? = null

        fun prepare(function: () -> Unit) {
            this.prepare = function
        }

        fun call(function: suspend () -> Model){
            this.call = function
        }

        fun success(function: (model: Model) -> Unit) {
            this.success = function
        }

        fun cache(function: (model: Model) -> Unit) {
            this.cache = function
        }

        fun failed(function: (error: Throwable) -> Unit) {
            this.failed = function
        }
    }

    class DoubleApiCallback<M1,M2>{
        internal var prepare: (() -> Unit)? = null
        internal lateinit var call: suspend (() -> M1)
        internal var success: ((m1: M1, m2: M2) -> Unit)? = null
        internal var failed: ((error: Throwable) -> Unit)? = null

        fun prepare(function: () -> Unit) {
            this.prepare = function
        }

        fun call(function: suspend () -> M1){
            this.call = function
        }

        fun success(function: (m1: M1, m2: M2) -> Unit) {
            this.success = function
        }

        fun failed(function: (error: Throwable) -> Unit) {
            this.failed = function
        }
    }
}
