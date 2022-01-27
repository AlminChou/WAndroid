package com.almin.arch.network

interface RetrofitConfiguration {
    abstract val connectTimeout: Long
    abstract val readTimeout: Long
    abstract val writeTimeout: Long
    abstract val callTimeout: Long
    abstract val servicesHost: String
    abstract val baseUrl: String
    abstract val tokenProvider: TokenProvider
}