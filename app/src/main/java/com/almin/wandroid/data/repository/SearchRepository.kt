package com.almin.wandroid.data.repository

import com.almin.arch.repository.Repository
import com.almin.wandroid.data.db.AppDataBase
import com.almin.wandroid.data.network.api.ArticleApiService

/**
 * Created by Almin on 2022/4/14.
 */
class SearchRepository(private val articleApiService: ArticleApiService, private val appDataBase: AppDataBase) : Repository() {

    suspend fun getHotKeyList() = articleApiService.getSearchHotKey()

}

