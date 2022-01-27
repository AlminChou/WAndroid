package com.almin.arch.middleware

/**
 * Created by Almin on 2022/1/8.
 */
interface ResourceProvider {
    fun getString(resourceId: Int): String
    fun getString(resourceId: Int, vararg args: Any): String
}