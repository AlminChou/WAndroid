package com.almin.wandroid.ui.module.common

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
import com.almin.wandroid.data.repository.ProjectRepository
import com.almin.wandroid.ui.module.portal.PortalContract.Companion.TYPE_NAVIGATION
import com.almin.wandroid.ui.module.portal.PortalContract.Companion.TYPE_SYSTEM
import kotlinx.coroutines.delay

class ArticleFeedViewModel(private val articleRepository: ArticleRepository, private val projectRepository: ProjectRepository) : AbstractViewModel<ArticleFeedContract.PageState, ArticleFeedContract.PageEvent, Contract.PageEffect>(null) {

    private var pageIndex = 0
    private var feedType = -1
    private var cid = -1

    override fun initialState(): ArticleFeedContract.PageState = ArticleFeedContract.PageState()

    override fun attach(arguments: Bundle?) {
        arguments?.run {
            feedType = getInt(Key.BUNDLE_KEY_ARTICLE_FEED_TYPE)
            cid = getInt(Key.BUNDLE_KEY_ARTICLE_FEED_CID, -1)
        }
    }

    override fun handleEvent(event: ArticleFeedContract.PageEvent) {
        when(event){
            is ArticleFeedContract.PageEvent.LoadFeedList -> {
                loadFeed(!event.isLoadMore)
            }
        }
    }

    private fun loadFeed(refresh: Boolean) {
        if(refresh){
            pageIndex = 0
        }
        api<PagerResponse<Article>>{
            call {
                delay(1000)
                when(feedType){
                    Article.ARTICLE_TYPE_HOME -> articleRepository.getHomeFeedList(pageIndex)
                    Article.ARTICLE_TYPE_SQUARE -> articleRepository.getSquareFeedList(pageIndex)
                    Article.ARTICLE_TYPE_SYSTEM_LIST -> articleRepository.getSystemFeedList(pageIndex, cid)
                    Article.ARTICLE_TYPE_PROJECT -> {
                        if(cid == 0)
                            projectRepository.getLatestProjectList(pageIndex)
                        else
                            projectRepository.getProjectListByCategoryId(cid, pageIndex)
                    }
                    else -> articleRepository.getSquareFeedList(pageIndex)
                }
            }
            prepare {
                setState { copy(loadStatus = if(refresh) LoadStatus.Refresh else LoadStatus.LoadMore)}
            }
            success {

                if(feedType == Article.ARTICLE_TYPE_PROJECT){
                    pageIndex = if(cid == 0) it.curPage else it.curPage + 1
                }else{
                    pageIndex = it.curPage
                }

                setState { copy(articles = it.datas, loadStatus = if(refresh) LoadStatus.Finish else LoadStatus.LoadMoreFinish)}
            }
            failed {
                setState { copy(loadStatus = if(refresh) LoadStatus.LoadFailed else LoadStatus.LoadMoreFailed)}
            }
        }
    }

}