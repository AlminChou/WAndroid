package com.almin.wandroid.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.almin.arch.repository.Repository
import com.almin.wandroid.data.db.AppDataBase
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Article.Companion.ARTICLE_TYPE_HOME
import com.almin.wandroid.data.model.Banner
import com.almin.wandroid.data.model.PagerResponse
import com.almin.wandroid.data.model.RemoteKeys
import com.almin.wandroid.data.network.api.ArticleApiService
import com.almin.wandroid.ui.paging.remoteMediatorPager
import kotlinx.coroutines.flow.Flow

/**
 * Created by Almin on 2022/1/11.
 */
class ArticleRepository(private val articleApiService: ArticleApiService, private val appDataBase: AppDataBase) : Repository() {

    suspend fun getBanner(): List<Banner> {
        return articleApiService.getBanner()
    }

    suspend fun getTopArticles() = articleApiService.getTopArticleList()

    suspend fun getArticleList(page: Int) = articleApiService.getArticleList(page).datas


    fun homePager() : Flow<PagingData<Article>> {
        return remoteMediatorPager(config = PagingConfig(15, 1, true, 15), pagingSourceFactory = {
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


    suspend fun getSquareFeedList(pageIndex: Int) : PagerResponse<Article> {
        val rsp = articleApiService.getSquareData(pageIndex)
        rsp.datas.map { it.articleType = Article.ARTICLE_TYPE_SQUARE }
        return rsp
    }
}

