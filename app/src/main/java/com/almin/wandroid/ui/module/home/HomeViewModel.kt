package com.almin.wandroid.ui.module.home

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.almin.arch.ktx.launch
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.Contract.PageEvent
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Banner
import com.almin.wandroid.ui.paging.pager
import com.almin.wandroid.data.repository.ArticleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(middleWareProvider: MiddleWareProvider, private val articleRepository: ArticleRepository) : AbstractViewModel<PageState, PageEvent, Contract.PageEffect>(middleWareProvider) {

    // 不带缓存方式
    private val articlePager by lazy {
        pager(config = PagingConfig(15, 1, true, 15), 0){
            articleRepository.getArticleList(it)
        }
    }

    // 使用room缓存方式
    private val articlePagerWithRoom by lazy {
        articleRepository.homePager().cachedIn(viewModelScope)
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

//    var viewStates by mutableStateOf(HomeContract.PageState(articlePagerWithRoom))
    var viewStates by mutableStateOf(HomeContract.PageState(articlePager))
        private set

    override fun attach(arguments: Bundle?) {

    }

    private fun loadFeed(){
        // simple request demo
//        api<List<Banner>> {
//            call{
//                articleRepository.getBanner()
//            }
//            success{
//                viewStates = viewStates.copy(banners = it)
//            }
//            failed{
//                println("112312321321321321   ${it.toString()}")
//            }
//        }

        val bannerFlow = flow<List<Banner>> {
//            throw Exception("test ")
            emit(articleRepository.getBanner())
        }.apiCatch { emit(emptyList()) }

        val articlesFlow = flow<List<Article>> {
            emit(articleRepository.getTopArticles())
        }.apiCatch { emit(emptyList()) }

        launch {
            bannerFlow.zip(articlesFlow){ bannerList, articleList ->
                delay(1000)
                _isRefreshing.emit(false)
                viewStates = viewStates.copy(banners = bannerList, topArticles = articleList, loadStatus = LoadStatus.Finish)
            }.onStart {
                _isRefreshing.emit(true)
                viewStates = viewStates.copy(loadStatus = LoadStatus.Loading)
            }.catch {
                delay(1000)
                viewStates = viewStates.copy(loadStatus = LoadStatus.LoadFailed)
                _isRefreshing.emit(false)
            }.collect()
        }

    }

    override fun handleEvent(event: PageEvent) {
        when(event){
            HomeContract.PageEvent.Refresh -> {
                // This doesn't handle multiple 'refreshing' tasks, don't use this
                loadFeed()
            }
        }
    }

    override fun initialState(): PageState = PageState.Default


}