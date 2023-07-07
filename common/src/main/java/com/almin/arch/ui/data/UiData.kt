package com.almin.arch.ui.data

import kotlinx.coroutines.flow.MutableStateFlow

sealed class LoadStatus {
    object Default : LoadStatus()
    object Loading : LoadStatus()
    object Finish : LoadStatus()
    object Empty : LoadStatus()
    object NoMore : LoadStatus()
    object LoadFailed : LoadStatus()
    object LoadMore : LoadStatus()
    object LoadMoreFailed : LoadStatus()
    object LoadMoreFinish : LoadStatus()
    object LoadMoreEnd : LoadStatus()
    object Refresh : LoadStatus()
    object RefreshFailed : LoadStatus()
    object RefreshFinish : LoadStatus()

    companion object {
        fun loading(refresh: Boolean = false, loadMore: Boolean = false): LoadStatus {
            return when {
                refresh -> Refresh
                loadMore -> LoadMore
                else -> Loading
            }
        }

        fun finish(refresh: Boolean = false, loadMore: Boolean = false): LoadStatus {
            return when {
                refresh -> RefreshFinish
                loadMore -> LoadMoreFinish
                else -> Finish
            }
        }

        fun noData(refresh: Boolean = false, loadMore: Boolean = false): LoadStatus {
            return when {
                refresh -> Empty
                loadMore -> LoadMoreEnd
                else -> Empty
            }
        }

        fun failed(refresh: Boolean = false, loadMore: Boolean = false): LoadStatus {
            return when {
                refresh -> RefreshFailed
                loadMore -> LoadMoreFailed
                else -> LoadFailed
            }
        }
    }

}


data class UiData<T>(
    var value: T? = null,
    var loadStatus: LoadStatus = LoadStatus.Default,
    var message: String? = null,
    var throwable: Throwable? = null
)

// update value
fun <T> MutableStateFlow<UiData<T>>.updateValue(action: UiData<T>.() -> UiData<T>) {
    value = value.action()
}

fun <T> MutableStateFlow<UiData<T>>.empty(data: T?) {
    value = value.copy(loadStatus = LoadStatus.Empty, value = data, throwable = null, message = null)
}

fun <T> MutableStateFlow<UiData<T>>.finish(
    refresh: Boolean = false,
    loadMore: Boolean = false,
    data: T?
) {
    value = value.copy(loadStatus = LoadStatus.finish(refresh, loadMore), value = data, throwable = null, message = null)
}

fun <T> MutableStateFlow<UiData<T>>.failed(
    refresh: Boolean = false,
    loadMore: Boolean = false,
    message: String?,
    throwable: Throwable? = null
) {
    value = value.copy(loadStatus = LoadStatus.failed(refresh, loadMore), message = message, throwable = throwable)
}

// loading状态 数据null
fun <T> MutableStateFlow<UiData<T>>.loading(refresh: Boolean = false, loadMore: Boolean = false) {
    value = value.copy(loadStatus = LoadStatus.loading(refresh, loadMore), throwable = null, message = null)
}
// 同上， 刷新和loadMore 必定2选1
fun <T> MutableStateFlow<UiData<T>>.refreshOrMoreLoading(refreshOrLoadMore: Boolean) {
    value = value.copy(loadStatus = LoadStatus.loading(refreshOrLoadMore, !refreshOrLoadMore), throwable = null, message = null)
}

// loading 状态 带数据，可能是默认数据 或者缓存
fun <T> MutableStateFlow<UiData<T>>.loadingWithValue(
    refresh: Boolean = false,
    loadMore: Boolean = false,
    action: UiData<T>.() -> UiData<T>
) {
    value = value.copy(loadStatus = LoadStatus.loading(refresh, loadMore), throwable = null, message = null).action()
}


