package com.almin.arch.network.urlprocessor

/**
 * Created by Almin on 2019-07-03.
 */
interface BaseUrlProvider {
    fun getBaseUrl(key: String?) : String?
}