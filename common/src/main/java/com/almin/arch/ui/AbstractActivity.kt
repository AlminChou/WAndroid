package com.almin.arch.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.almin.arch.bus.FlowBus
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract.PageEffect
import com.almin.arch.viewmodel.Contract.PageState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2019-07-01.
 */
abstract class AbstractActivity<VB : ViewBinding, S: PageState, Effect: PageEffect,  VM : AbstractViewModel<S,*,Effect>>(private val inflate: (LayoutInflater) -> VB) : AppCompatActivity(){

    private lateinit var binding: VB
    protected abstract val viewModel: VM

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun handleState(state: S)
    protected abstract fun handleEffect(effect: Effect)

//    protected abstract fun initEventSubscribe(bus: FlowBus)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.attach(intent?.extras)
        initView()

        bind(Lifecycle.State.STARTED) {
            viewModel.uiState.collect(this@AbstractActivity::handleState)
        }
        bind(Lifecycle.State.STARTED){
            viewModel.effect.collect {
                handleEffect(it)
            }
        }
        initData()
//        initEventSubscribe(FlowBus())
    }

    fun bind(state: Lifecycle.State, action: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(state) {
                action()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}