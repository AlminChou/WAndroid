package com.almin.wandroid.data.network.api

import com.almin.wandroid.data.model.NavigationNode
import com.almin.wandroid.data.model.SystemNode
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Almin on 2022/3/29.
 */
interface PortalApiService {

    /**
     * 获取体系数据
     */
    @GET("tree/json")
    suspend fun getSystemTree() : List<SystemNode>


    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationTree(): List<NavigationNode>

}