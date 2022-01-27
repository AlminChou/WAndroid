package com.almin.arch.bus

import androidx.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*lifecycleScope.launch(Dispatchers.IO) {
    observeEvent {
        LjyLogUtil.d("FlowBus.register1:${GsonUtils.toJson(it)}_${Thread.currentThread().name}")
    }
    observeEvent(Dispatchers.IO) {
        LjyLogUtil.d("FlowBus.register2:${GsonUtils.toJson(it)}_${Thread.currentThread().name}")
    }

    observeEvent(Dispatchers.Main) {
        LjyLogUtil.d("FlowBus.register3:${GsonUtils.toJson(it)}_${Thread.currentThread().name}")
    }
}

lifecycleScope.launch(Dispatchers.IO) {
    delay(1000)
    postValue(EventMessage(100))
    postValue(EventMessage(101), 1000)
    postValue(EventMessage(102, "bbb"), dispatcher = Dispatchers.IO)
    val event3 = EventMessage(103, "ccc")
    event3.put("key1", 123)
    event3.put("key2", "abc")
    postValue(event3, 2000, dispatcher = Dispatchers.Main)
}*/

class FlowBus : ViewModel() {
    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { FlowBus() }
    }

    //正常事件
    private val events = mutableMapOf<String, Event<*>>()

    //粘性事件
    private val stickyEvents = mutableMapOf<String, Event<*>>()

    fun with(key: String, isSticky: Boolean = false): Event<Any> {
        return with(key, Any::class.java, isSticky)
    }

    fun <T> with(eventType: Class<T>, isSticky: Boolean = false): Event<T> {
        return with(eventType.name, eventType, isSticky)
    }

    @Synchronized
    fun <T> with(key: String, type: Class<T>?, isSticky: Boolean): Event<T> {
        val flows = if (isSticky) stickyEvents else events
        if (!flows.containsKey(key)) {
            flows[key] = Event<T>(key, isSticky)
        }
        return flows[key] as Event<T>
    }


    class Event<T>(private val key: String, isSticky: Boolean) {

        // private mutable shared flow
        private val _events = MutableSharedFlow<T>(
            replay = if (isSticky) 1 else 0,
            extraBufferCapacity = Int.MAX_VALUE
        )

        // publicly exposed as read-only shared flow
        val events = _events.asSharedFlow()

        /**
         * need main thread execute
         */
        fun observeEvent(
            lifecycleOwner: LifecycleOwner,
            dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
            minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
            action: (t: T) -> Unit
        ) {
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    val subscriptCount = _events.subscriptionCount.value
                    if (subscriptCount <= 0)
                        instance.events.remove(key)
                }
            })
            lifecycleOwner.lifecycleScope.launch(dispatcher) {
                lifecycleOwner.lifecycle.whenStateAtLeast(minActiveState) {
                    events.collect {
                        try {
                            action(it)
                        } catch (e: Exception) {
                            LogUtils.d("ker=$key , error=${e.message}")
                        }
                    }
                }
            }
        }

        /**
         * send value
         */
        suspend fun setValue(
            event: T,
            dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
        ) {
            withContext(dispatcher) {
                _events.emit(event)
            }

        }
    }
}