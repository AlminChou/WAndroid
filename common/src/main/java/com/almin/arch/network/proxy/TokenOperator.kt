package com.almin.arch.network.proxy

import com.almin.arch.network.TokenProvider


interface TokenOperator : TokenProvider {
    val lastUpdateTokenTime: Long
    // need to synchronized
    fun updateToken(token: String)
    fun updateUpdateTokenLastTime(updateTokenLastTime: Long)
    fun callRefreshUserTokenApi(): String?
}