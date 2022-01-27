package com.almin.arch.middleware

/**
 * Created by Almin on 2022/1/12.
 */
interface MiddleWareProvider {
    fun resourceProvider(): ResourceProvider
    fun exceptionHandlerProvider(): ExceptionHandler
}