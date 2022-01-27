package com.almin.arch.network.exception

/**
 * Created by Almin on 2019/4/14.
 */
class BusinessServiceException(val code: Int = 0, message: String?, val responseJson: String? = null) : RuntimeException(message)
