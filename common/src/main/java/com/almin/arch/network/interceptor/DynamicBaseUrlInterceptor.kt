package com.almin.arch.network.interceptor

import android.util.Log
import com.almin.arch.network.exception.InvalidUrlException
import com.almin.arch.network.urlprocessor.UrlProcessor
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Almin on 2017/12/25.
 */

class DynamicBaseUrlInterceptor(private val urlProcessor: UrlProcessor) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        var requestUrl = request.url.toString()

        val baseUrl: String? = urlProcessor.getBaseUrl(request.url.encodedPath)
        Log.d("Http request", String.format("Request http url base url change ：%s ", baseUrl))


        if (baseUrl != null) {
//            val baseHttpUrl = baseUrl.toHttpUrlOrNull() ?: throw InvalidUrlException(baseUrl)
            val baseHttpUrl = baseUrl.toHttpUrlOrNull() ?: throw InvalidUrlException(baseUrl)
            val requestHttpUrl = urlProcessor.wrapUrl(baseHttpUrl, request.url)
            requestBuilder.url(requestHttpUrl)
            requestUrl = requestHttpUrl.toString()
        }

        urlProcessor.addHeader(requestBuilder)

        Log.d("Http request", String.format("Request http url is ： %s", requestUrl))

        return chain.proceed(requestBuilder.build())
    }

}