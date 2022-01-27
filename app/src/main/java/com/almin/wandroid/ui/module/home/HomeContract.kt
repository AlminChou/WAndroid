package com.almin.wandroid.ui.module.home

import androidx.paging.PagingData
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Banner
import kotlinx.coroutines.flow.Flow

/**
 * Created by Almin on 2022/1/12.
 */
interface HomeContract {
    data class PageState(val articlePaging: Flow<PagingData<Article>>, var banners: List<Banner>? = null, var topArticles: List<Article>? = null, override val loadingState: Contract.LoadStatus = Contract.LoadStatus.Loading) : Contract.PageState


    sealed class PageEvent: Contract.PageEvent{
        object Refresh: PageEvent()
    }

}