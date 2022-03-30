package com.almin.wandroid.data.network.api

import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.ArticlePageRsp
import com.almin.wandroid.data.model.Banner
import com.almin.wandroid.data.model.PagerResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Almin on 2022/1/11.
 */
interface ArticleApiService {
    /**
     * banner数据
     */
    @GET("banner/json")
    suspend fun getBanner(): List<Banner>

    /**
     * 置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopArticleList(): List<Article>


    /**
     * 首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): ArticlePageRsp


    /*
     * 广场列表
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareData(@Path("page") page: Int): PagerResponse<Article>


}