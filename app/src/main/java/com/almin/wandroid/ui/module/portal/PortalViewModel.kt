package com.almin.wandroid.ui.module.portal

import android.os.Bundle
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.const.Key
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.PagerResponse
import com.almin.wandroid.data.model.TagTree
import com.almin.wandroid.data.repository.ArticleRepository
import com.almin.wandroid.data.repository.PortalRepository
import com.almin.wandroid.ui.module.portal.PortalContract.Companion.TYPE_NAVIGATION
import com.almin.wandroid.ui.module.portal.PortalContract.Companion.TYPE_SYSTEM
import kotlinx.coroutines.delay

class PortalViewModel(private val articleRepository: ArticleRepository,
                      private val portalRepository: PortalRepository) : AbstractViewModel<PortalContract.PageState, PortalContract.PageEvent, Contract.PageEffect>(null) {

    private var pageIndex = 0
    private var tagType = -1

    override fun initialState(): PortalContract.PageState = PortalContract.PageState()

    override fun attach(arguments: Bundle?) {
        arguments?.run {
            tagType = this.getInt(Key.BUNDLE_KEY_TAG_PAGE_TYPE)
        }
    }

    override fun handleEvent(event: PortalContract.PageEvent) {
        when(event){
            is PortalContract.PageEvent.LoadSquareList -> {
                loadSquareFeed(!event.isLoadMore)
            }

            is PortalContract.PageEvent.LoadTagTree -> {
                loadTagTree(!event.isLoadMore)
            }
        }
    }

    private fun loadTagTree(refresh: Boolean) {
//        api<List<TagTree>> {
//            call {
//                when(tagType){
//                    TYPE_SYSTEM -> {
//                        portalRepository.loadSystemTree()
//                    }
//                    else -> {
//                        portalRepository.loadNavigationTree()
//                    }
//                }
//            }
//        }
    }

    private fun loadSquareFeed(refresh: Boolean) {
        if(refresh){
            pageIndex = 0
        }
        api<PagerResponse<Article>>{
            call {
                delay(1000)
                articleRepository.getSquareFeedList(pageIndex)
            }
            prepare {
                setState { copy(loadStatus = if(refresh) LoadStatus.Refresh else LoadStatus.LoadMore)}
            }
            success {
                pageIndex = it.curPage
                setState { copy(articles = it.datas, loadStatus = if(refresh) LoadStatus.Finish else LoadStatus.LoadMoreFinish)}
            }
            failed {
                setState { copy(loadStatus = if(refresh) LoadStatus.LoadFailed else LoadStatus.LoadMoreFailed)}
            }
        }
    }

}