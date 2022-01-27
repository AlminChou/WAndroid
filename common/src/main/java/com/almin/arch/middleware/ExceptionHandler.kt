package com.almin.arch.middleware

/**
 * Created by Almin on 2022/1/4.
 */
interface ExceptionHandler {
    fun handle(throwable: Throwable)
}