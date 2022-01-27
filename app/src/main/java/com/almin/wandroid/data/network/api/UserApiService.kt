package com.almin.wandroid.data.network.api

import com.almin.wandroid.data.model.UserInfo
import okhttp3.ResponseBody
import retrofit2.Response

import retrofit2.http.*
interface UserApiService {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): UserInfo

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String, @Field("password") pwd: String, @Field("repassword") rpwd: String
    ): UserInfo


}