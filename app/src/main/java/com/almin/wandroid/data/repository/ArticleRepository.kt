package com.almin.wandroid.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.almin.arch.repository.Repository
import com.almin.wandroid.data.db.AppDataBase
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Banner
import com.almin.wandroid.data.model.RemoteKeys
import com.almin.wandroid.data.network.api.ArticleApiService
import com.almin.wandroid.ui.paging.remoteMediatorPage
import kotlinx.coroutines.flow.Flow

/**
 * Created by Almin on 2022/1/11.
 */
class ArticleRepository(private val articleApiService: ArticleApiService, private val appDataBase: AppDataBase) : Repository() {
    companion object{
        const val ARTICLE_TYPE_HOME = 0
        const val ARTICLE_TYPE_COLLECT = 1
    }

    suspend fun getBanner(): List<Banner> {
        return articleApiService.getBanner()
    }

    suspend fun getTopArticles() = articleApiService.getTopArticleList()

    suspend fun getArticleList(page: Int) = articleApiService.getArticleList(page).datas


    fun homePager() : Flow<PagingData<Article>> {
        return remoteMediatorPage(config = PagingConfig(15, 1, true, 15), pagingSourceFactory = {
            appDataBase.article.queryArticleCache(ARTICLE_TYPE_HOME)
        }, keyQuery = {
            appDataBase.remoteKeys.getRemoteKeys(ARTICLE_TYPE_HOME)
        }, dbInsert = { db , preKey, nextKey , value ->
            value.forEach {
                it.articleType = ARTICLE_TYPE_HOME
            }
            appDataBase.remoteKeys.insert(RemoteKeys(articleType = ARTICLE_TYPE_HOME, prevKey = preKey, nextKey = nextKey))
            appDataBase.article.saveListCache(value)
        }, dbClean = {
            appDataBase.article.clearArticleByType(ARTICLE_TYPE_HOME)
            appDataBase.remoteKeys.clearRemoteKeys(ARTICLE_TYPE_HOME)
        }, db = appDataBase, apiCall = {
            getArticleList(it)
        })
    }

}