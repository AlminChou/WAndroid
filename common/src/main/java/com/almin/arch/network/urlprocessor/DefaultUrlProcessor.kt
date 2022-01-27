package com.almin.arch.network.urlprocessor

import okhttp3.HttpUrl
import okhttp3.Request


/**
 * Created by Almin on 2017/12/25.
 */

open class DefaultUrlProcessor : UrlProcessor {

    override fun getBaseUrl(url: String): String? = null


    override val scheme: String
        get() = "https"

    override val host: String ?
        get() = null

    override fun addParameter(builder: HttpUrl.Builder) {}

    override fun addHeader(builder: Request.Builder) {
        builder.addHeader("Accept", "application/json")
        builder.addHeader("Content-Type", "application/json")
        //        builder.addHeader("User-Agent", "");
    }


    override fun wrapUrl(baseHttpUrl: HttpUrl ?, url: HttpUrl): HttpUrl {
        if (null == baseHttpUrl) {
            return url
        }

        val builder = url.newBuilder()
        addParameter(builder)

        return builder.scheme(baseHttpUrl.scheme)
            .host(baseHttpUrl.host)
            .port(baseHttpUrl.port)
            .build()
    }
}