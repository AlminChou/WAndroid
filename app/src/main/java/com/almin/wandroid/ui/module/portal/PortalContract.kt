package com.almin.wandroid.ui.module.portal

import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.Article

/**
 * Created by Almin on 2022/3/18.
 */
interface PortalContract {

    companion object{
        const val TYPE_SYSTEM = 0
        const val TYPE_NAVIGATION = 1
    }

    sealed class PageEvent : Contract.PageEvent{
        data class LoadSquareList(val isLoadMore: Boolean) : PageEvent()
        data class LoadTagTree(val isLoadMore: Boolean) : PageEvent()
    }

    data class PageState(val articles: List<Article>? = null, val loadStatus: LoadStatus = LoadStatus.Default) : Contract.PageState()
}