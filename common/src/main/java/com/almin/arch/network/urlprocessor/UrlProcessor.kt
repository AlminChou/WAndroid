package com.almin.arch.network.urlprocessor

import okhttp3.HttpUrl
import okhttp3.Request

/**
 * Created by Almin on 2017/12/25.
 */

interface UrlProcessor {

    val scheme: String ?

    val host: String ?

    fun getBaseUrl(url: String): String?

    fun addParameter(builder: HttpUrl.Builder)

    fun addHeader(builder: Request.Builder)

    fun wrapUrl(baseHttpUrl: HttpUrl?, url: HttpUrl): HttpUrl

}