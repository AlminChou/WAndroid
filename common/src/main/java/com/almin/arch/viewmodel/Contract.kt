package com.almin.arch.viewmodel

/**
 * Created by Almin on 2022/1/6.
 */
interface Contract {

    interface PageEvent{
        sealed interface Default: PageEvent
    }
    interface PageState{
        object Default: PageState{
            override val loadingState: LoadStatus = LoadStatus.Default
        }
        val loadingState: LoadStatus
    }

    sealed class LoadStatus {
        object Default: LoadStatus()
        object Loading: LoadStatus()
        object Finish: LoadStatus()
        object LoadFailed: LoadStatus()
        object LoadMore: LoadStatus()
        object LoadMoreFailed: LoadStatus()
        object LoadMoreFinish: LoadStatus()
        object Refresh: LoadStatus()
        object RefreshFailed: LoadStatus()
        object RefreshFinish: LoadStatus()
    }
}