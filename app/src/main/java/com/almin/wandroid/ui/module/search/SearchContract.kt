package com.almin.wandroid.ui.module.search

import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.HotKey

/**
 * Created by Almin on 2022/1/12.
 */
interface SearchContract {
    data class PageState(var hotKeys: List<HotKey>? = null,
                         val history: List<String>? = null,
                         var topArticles: List<Article>? = null, val loadStatus: LoadStatus = LoadStatus.Default) : Contract.PageState()


    sealed class PageEvent: Contract.PageEvent{
        object Refresh: PageEvent()
        object CleanHistory: PageEvent()
    }

}