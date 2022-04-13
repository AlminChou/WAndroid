package com.almin.wandroid.ui.module.common

import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.Article

/**
 * Created by Almin on 2022/4/13.
 */
interface ArticleFeedContract {

    sealed class PageEvent : Contract.PageEvent{
        data class LoadFeedList(val isLoadMore: Boolean) : PageEvent()
    }

    data class PageState(val articles: List<Article>? = null, val loadStatus: LoadStatus = LoadStatus.Default) : Contract.PageState()

}