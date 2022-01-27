package com.almin.arch.bus

import android.app.Application
import androidx.lifecycle.*
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2022/1/10.
 */
fun LifecycleOwner.observeEvent(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    action: (t: EventMessage) -> Unit
) {
    ApplicationScopeViewModelProvider
        .getApplicationScopeViewModel(FlowBus::class.java)
        .with(EventMessage::class.java, isSticky = isSticky)
        .observeEvent(this@observeEvent, dispatcher, minActiveState, action)
}

fun postValue(
    event: EventMessage,
    delayTimeMillis: Long = 0,
    isSticky: Boolean = false,
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
) {
    LogUtils.d("FlowBus:send_${Thread.currentThread().name}_${GsonUtils.toJson(event)}")
    ApplicationScopeViewModelProvider
        .getApplicationScopeViewModel(FlowBus::class.java)
        .viewModelScope
        .launch(dispatcher) {
            delay(delayTimeMillis)
            ApplicationScopeViewModelProvider
                .getApplicationScopeViewModel(FlowBus::class.java)
                .with(EventMessage::class.java, isSticky = isSticky)
                .setValue(event)
        }
}

private object ApplicationScopeViewModelProvider : ViewModelStoreOwner {

    private val eventViewModelStore: ViewModelStore = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return eventViewModelStore
    }

    private val mApplicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(
            ApplicationScopeViewModelProvider,
            ViewModelProvider.AndroidViewModelFactory.getInstance(FlowBusInitializer.application)
        )
    }

    fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        return mApplicationProvider[modelClass]
    }

}

object FlowBusInitializer {
    lateinit var application: Application
    //在Application中初始化
    fun init(application: Application) {
        FlowBusInitializer.application = application
    }
}