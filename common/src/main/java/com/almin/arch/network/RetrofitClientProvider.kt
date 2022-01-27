package com.almin.arch.network

import com.almin.arch.network.interceptor.ConnectivityInterceptor
import com.almin.arch.network.proxy.TokenOperator
import com.almin.arch.network.ssl.TrustAllSSLSocketFactory
import com.almin.arch.scheduler.SchedulersProvider
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Proxy
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 * Created by Almin on 2020/5/20.
 */
class RetrofitClientProvider private constructor(private val retrofit: Retrofit) {

    class Builder{
        private var baseUrl: String
        private var retrofitConfiguration: RetrofitConfiguration? = null

        constructor(retrofitConfiguration: RetrofitConfiguration){
            this.retrofitConfiguration = retrofitConfiguration
            this.baseUrl = retrofitConfiguration.baseUrl
        }

        constructor(baseUrl: String, configDsl: (ConfigDsl.() -> Unit)?){
            this.baseUrl = baseUrl
            val configDslProxy = ConfigDsl().apply {
                configDsl?.invoke(this)
            }
            configDslProxy.okhttpBuilderDsl?.invoke(okHttpClientBuilder)
            configDslProxy.retrofitBuilderDsl?.invoke(retrofitBuilder)
        }

        private var okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
            retrofitConfiguration?.run {
                //  default in okhttp
//            callTimeout = 0
//            connectTimeout = 10000
//            readTimeout = 10000
//            writeTimeout = 10000
                connectTimeout(connectTimeout * 1000, TimeUnit.MILLISECONDS)
                readTimeout(readTimeout * 1000, TimeUnit.MILLISECONDS)
                callTimeout(callTimeout * 1000, TimeUnit.MILLISECONDS)
                writeTimeout(writeTimeout * 1000, TimeUnit.MILLISECONDS)
            }


//            if (BuildConfig.DEBUG) {
                // Install the all-trusting trust manager
                sslSocketFactory(TrustAllSSLSocketFactory.newInstance(), TrustAllSSLSocketFactory.TrustAllCertsManager())
                hostnameVerifier(HostnameVerifier { hostname, session -> true })
//            }
//            configDslProxy?.okhttpBuilderDsl?.invoke(this)
        }

        private var retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
            // kotlin Coroutine adapter & serialization converter
//            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())


        fun addInterceptor(interceptor: Interceptor): Builder{
            okHttpClientBuilder.addInterceptor(interceptor)
            return this
        }

        fun addConverterFactory(converterFactory: Converter.Factory): Builder{
            retrofitBuilder.addConverterFactory(converterFactory)
            return this
        }

        fun addCallAdapterFactory(callAdapterFactory: CallAdapter.Factory): Builder{
            retrofitBuilder.addCallAdapterFactory(callAdapterFactory)
            return this
        }

        fun networkChecker(networkChecker: NetworkChecker): Builder{
            addInterceptor(ConnectivityInterceptor(networkChecker))
            return this
        }

        fun build(): RetrofitClientProvider {
            if(baseUrl.isEmpty()){
                throw NullPointerException("RetrofitClientProvider baseUrl can not be empty! ")
            }
            retrofitBuilder.baseUrl(baseUrl)
            return RetrofitClientProvider(retrofitBuilder.client(okHttpClientBuilder.build()).build())
        }

        class ConfigDsl{
            internal var okhttpBuilderDsl: ((builder: OkHttpClient.Builder) -> OkHttpClient.Builder)? = null
            internal var retrofitBuilderDsl: ((builder: Retrofit.Builder) -> Retrofit.Builder)? = null

            fun okHttp(builder: ((OkHttpClient.Builder) -> OkHttpClient.Builder)?) {
                this.okhttpBuilderDsl = builder
            }

            fun retrofit(builder: ((Retrofit.Builder) -> Retrofit.Builder)?) {
                this.retrofitBuilderDsl = builder
            }
        }
    }

    fun <T> createService(clazz: Class<T>): T = retrofit.create(clazz)

//    fun <T : Any> createServiceWithTokenProxy(clazz: Class<T>, tokenOperator: TokenOperator, schedulersProvider: SchedulersProvider): T {
//        val originalService: Any = retrofit.create(clazz) as Any
//        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz), TokenRefreshProxy(originalService, tokenOperator, schedulersProvider)) as T
//    }


    companion object {
        fun createCustomJsonRequestBody(json: String): RequestBody {
//            return json.toRequestBody("application/json".toMediaType())
            return json.toRequestBody("application/json".toMediaTypeOrNull())
        }
    }

}