package com.almin.wandroid.data.repository

import com.almin.arch.repository.Repository
import com.almin.wandroid.data.model.Banner
import com.almin.wandroid.data.network.api.ArticleApiService

/**
 * Created by Almin on 2022/1/11.
 */
class ArticleRepository(private val articleApiService: ArticleApiService) : Repository() {
    suspend fun getBanner(): List<Banner> {
        return articleApiService.getBanner()
    }

    suspend fun getTopArticles() = articleApiService.getTopArticleList()

    suspend fun getArticleList(page: Int) = articleApiService.getArticleList(page).datas


}