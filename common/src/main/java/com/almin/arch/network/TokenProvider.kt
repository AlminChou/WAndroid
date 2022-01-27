package com.almin.arch.network

interface TokenProvider{
    fun getToken(): String?
}