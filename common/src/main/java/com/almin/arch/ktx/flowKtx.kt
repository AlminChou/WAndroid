package com.almin.arch.ktx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2023/7/5.
 */
fun <T> Flow<T>.collect(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State,
    action: suspend CoroutineScope.(T) -> Unit
) {
    lifecycleOwner.lifecycle.coroutineScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(state) {
            collect {
                action(it)
            }
        }
    }
}

fun <T> Flow<T>.collectLatest(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State,
    action: suspend CoroutineScope.(T) -> Unit
) {
    lifecycleOwner.lifecycle.coroutineScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(state) {
            collectLatest {
                action(it)
            }
        }
    }
}