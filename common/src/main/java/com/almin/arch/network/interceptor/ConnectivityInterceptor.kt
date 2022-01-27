package com.almin.arch.network.interceptor

import com.almin.arch.network.NetworkChecker
import com.almin.arch.network.exception.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Almin on 2017/12/25.
 */
class ConnectivityInterceptor(private val networkChecker: NetworkChecker) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkChecker.isNetworkActive()) {
            throw NoConnectivityException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}