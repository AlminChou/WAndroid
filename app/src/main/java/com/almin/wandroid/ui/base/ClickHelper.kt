package com.almin.wandroid.ui.base

import android.view.View
import androidx.compose.ui.Modifier
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2022/3/17.
 */

const val CLICK_INTERVAL = 300L
fun View.singleClick(interval: Long = CLICK_INTERVAL, onClick: () -> Unit){
    setOnClickListener(object : View.OnClickListener{
        private var lastTime: Long = 0L
        override fun onClick(p0: View?) {
            val currentTime = System.currentTimeMillis()
            if(currentTime - lastTime > interval){
                onClick()
                lastTime = currentTime
            }
        }
    })
}

fun BaseQuickAdapter<*, *>.singleItemClick(interval: Long = CLICK_INTERVAL, onClick: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit){
    setOnItemClickListener(object : OnItemClickListener {
        private var lastTime: Long = 0L
        override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
            val currentTime = System.currentTimeMillis()
            if(currentTime - lastTime > interval){
                onClick(adapter, view, position)
                lastTime = currentTime
            }
        }
    })
}

fun BaseQuickAdapter<*, *>.singleItemChildClick(interval: Long = CLICK_INTERVAL, onClick: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit){
    setOnItemChildClickListener(object : OnItemChildClickListener {
        private var lastTime: Long = 0L
        override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
            val currentTime = System.currentTimeMillis()
            if(currentTime - lastTime > interval){
                onClick(adapter, view, position)
                lastTime = currentTime
            }
        }
    })
}

class ClickHelper{
    companion object {
        private val debounceState = MutableStateFlow { }

        init {
            GlobalScope.launch(Dispatchers.Main) {
                debounceState
                    .debounce(CLICK_INTERVAL)
                    .collect { onClick ->
                        onClick.invoke()
                    }
            }
        }

        fun debounceClicks(onClick: () -> Unit) {
            debounceState.value = onClick
        }
    }
}
