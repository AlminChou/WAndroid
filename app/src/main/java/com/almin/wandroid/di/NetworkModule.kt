package com.almin.wandroid.di

import com.almin.arch.network.NetworkChecker
import com.almin.arch.network.RetrofitClientProvider
import com.almin.arch.network.RetrofitConfiguration
import com.almin.arch.network.TokenProvider
import com.almin.wandroid.data.network.api.ArticleApiService
import com.almin.wandroid.data.network.api.ProjectApiService
import com.almin.wandroid.data.network.converter.scalar.CustomScalarsConverterFactory
import com.almin.wandroid.data.network.api.UserApiService
import com.almin.wandroid.data.network.converter.type.CustomMoshiConverterFactory
import com.almin.wandroid.utils.NetworkUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module


/**
 * Created by Almin on 2020/5/19.
 */
const val SERVER_URL = "https://wanandroid.com/"
val networkModule = module{

    // dsl语法模式创建
    single {
        RetrofitClientProvider.Builder(SERVER_URL){
            okHttp {
                it
            }
            retrofit {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                it.addConverterFactory(CustomScalarsConverterFactory.create(moshi))
                it.addConverterFactory(CustomMoshiConverterFactory.create(moshi))
//                it.baseUrl("https://api.github.com/")
                it
            }
        }.networkChecker(object: NetworkChecker{
            override fun isNetworkActive(): Boolean {
                return NetworkUtil.isNetworkAvailable()
            }
        }).build()
    }


//    // 传统builder模式创建
//    single {
//        RetrofitClientProvider.Builder(get())
//            .addInterceptor()
//            .addCallAdapterFactory()
//            .addConverterFactory()
//            .build()
//    }

    single {
        object : RetrofitConfiguration{
            override val connectTimeout: Long = 5
            override val readTimeout: Long = 120
            override val writeTimeout: Long = 120
            override val callTimeout: Long = 0
            override val servicesHost: String = "https"
            // 只能以com 或者 端口结束
            override val baseUrl: String = SERVER_URL
            override val tokenProvider: TokenProvider = object : TokenProvider{
                override fun getToken(): String? {
                    return null
                }
            }
        }
    }

    single { get<RetrofitClientProvider>().createService(UserApiService::class.java) }
    single { get<RetrofitClientProvider>().createService(ArticleApiService::class.java) }
    single { get<RetrofitClientProvider>().createService(ProjectApiService::class.java) }
}