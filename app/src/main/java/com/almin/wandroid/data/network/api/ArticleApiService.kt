package com.almin.wandroid.data.network.api

import com.almin.wandroid.data.model.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getArticleList(@Path("page") page: Int): PagerResponse<Article>


    /*
     * 广场列表
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareList(@Path("page") page: Int): PagerResponse<Article>


    /**
     * 知识体系下的文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getSystemFeedList(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): PagerResponse<Article>


    /**
     * 根据关键词搜索数据
     */
    @POST("article/query/{page}/json")
    suspend fun getSearchDataByKey(
        @Path("page") pageNo: Int,
        @Query("k") searchKey: String
    ): PagerResponse<Article>


    /**
     * 获取热门搜索数据
     */
    @GET("hotkey/json")
    suspend fun getSearchHotKey(): List<HotKey>

}