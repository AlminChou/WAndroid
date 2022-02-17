package com.almin.wandroid.data.network.api

import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.PagerResponse
import com.almin.wandroid.data.model.Project
import com.almin.wandroid.data.model.ProjectCategory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Almin on 2022/2/16.
 */
interface ProjectApiService {
    /**
     * 分类标题
     */
    @GET("project/tree/json")
    suspend fun getProjectCategory(): List<ProjectCategory>

    /**
     * 分类id获取项目数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjecListByType(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ):  PagerResponse<Article>

    /**
     * 最新项目列表
     */
    @GET("article/listproject/{page}/json")
    suspend fun getLatestProjectList(@Path("page") pageNo: Int): PagerResponse<Article>

}