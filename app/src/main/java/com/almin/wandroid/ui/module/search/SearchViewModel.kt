package com.almin.wandroid.ui.module.search

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.almin.arch.ktx.launch
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.HotKey
import com.almin.wandroid.data.repository.ArticleRepository
import com.almin.wandroid.data.repository.SearchRepository
import kotlinx.coroutines.Dispatchers

/**
 * Created by Almin on 2022/4/13.
 */
class SearchViewModel(middleWareProvider: MiddleWareProvider, private val searchRepository: SearchRepository) : AbstractViewModel<SearchContract.PageState, SearchContract.PageEvent, Contract.PageEffect>(middleWareProvider) {

    var pageState by mutableStateOf(SearchContract.PageState())
        private set

    override fun initialState(): SearchContract.PageState = SearchContract.PageState()

    override fun attach(arguments: Bundle?) {

    }

    override fun handleEvent(event: SearchContract.PageEvent) {
        when(event){
            is SearchContract.PageEvent.Refresh -> {
                loadhotKeyList()
                loadSearchHistory()
            }

            is SearchContract.PageEvent.CleanHistory -> {
                cleanHistory()
            }
        }
    }

    private fun cleanHistory(){
        pageState = pageState.copy(history = emptyList())
        launch(Dispatchers.IO) {
            searchRepository.cleanHistory()
        }
    }

    private fun loadhotKeyList() {
        api<List<HotKey>> {
            call { searchRepository.getHotKeyList() }
            success {
//                        setState { copy(hotKeys = it) }
                pageState = pageState.copy(hotKeys = it)
            }
            failed {  }
        }
    }

    private fun loadSearchHistory(){
        cacheApi<List<String>> {
            call { searchRepository.getHistory() }
            success {
                pageState = pageState.copy(history = it)
            }
        }
    }
}