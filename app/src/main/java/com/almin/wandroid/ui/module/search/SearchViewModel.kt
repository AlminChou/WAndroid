package com.almin.wandroid.ui.module.search

import android.os.Bundle
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.HotKey
import com.almin.wandroid.data.repository.ArticleRepository
import com.almin.wandroid.data.repository.SearchRepository

/**
 * Created by Almin on 2022/4/13.
 */
class SearchViewModel(middleWareProvider: MiddleWareProvider, private val searchRepository: SearchRepository) : AbstractViewModel<SearchContract.PageState, SearchContract.PageEvent, Contract.PageEffect>(middleWareProvider) {

    override fun initialState(): SearchContract.PageState = SearchContract.PageState()

    override fun attach(arguments: Bundle?) {
        api<List<HotKey>> {
            call { searchRepository.getHotKeyList() }
            success {
                println("12312321321  ${it.size}")
            }
            failed {  }
        }
    }

    override fun handleEvent(event: SearchContract.PageEvent) {
    }
}