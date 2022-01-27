package com.almin.arch.network.exception

import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Almin on 2019-07-01.
 */
class ApiException(val code: Int, errorMsg: String?) : RuntimeException(errorMsg) {

    private var kind: Kind = Kind.HTTP
    var response: String? = null
    private var requestUrl: String? = null

    constructor(code: Int, errorMsg: String?, kind: Kind) : this(code, errorMsg){
        this.kind = kind
    }

    constructor(code: Int, errorMsg: String?, response: String?, kind: Kind) : this(code, errorMsg){
        this.kind = kind
        this.response = response
    }

    fun isTokenKind(): Boolean = kind == Kind.TOKEN
    fun isNetworkKind(): Boolean = kind == Kind.NETWORK
    fun isBusinessServiceKind(): Boolean = kind == Kind.BUSINESS_SERVICE
    fun isUnexpectedKind(): Boolean = kind == Kind.UNEXPECTED
    fun isCacheKind(): Boolean = kind == Kind.CACHE_NULL
    fun isNoConnectKind(): Boolean = kind == Kind.NO_CONNECT
    fun isHttpKind(): Boolean = kind == Kind.HTTP


    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>): T? {
        return null
    }

    companion object{
        const val ERROR_CODE_NO_CONNECT = -1
        const val ERROR_CODE_TOKEN = -2
        const val ERROR_CODE_UNEXPECTED = -3
        const val ERROR_CODE_NETWORK = -4
        const val ERROR_CODE_CACHE_NULL = -10

        fun tokenError(errorMsg: String?): ApiException {
            return ApiException(ERROR_CODE_TOKEN, errorMsg, null, Kind.TOKEN)
        }

        fun httpError(code: Int, errorMsg: String?, response: String? = null): ApiException {
            return ApiException(code, errorMsg, response, Kind.HTTP)
        }

        fun businessServiceError(code: Int, errorMsg: String?, response: String? = null): ApiException {
            return ApiException(code, errorMsg, response, Kind.BUSINESS_SERVICE)
        }

        fun networkError(errorMsg: String?): ApiException {
            return ApiException(ERROR_CODE_NETWORK, errorMsg, null, Kind.NETWORK)
        }

        fun unexpectedError(errorMsg: String?): ApiException {
            return ApiException(ERROR_CODE_UNEXPECTED, errorMsg, null, Kind.UNEXPECTED)
        }

        fun noConnectivityError(errorMsg: String?): ApiException {
            return ApiException(ERROR_CODE_NO_CONNECT, errorMsg, null, Kind.NO_CONNECT)
        }

        fun noCacheError(errorMsg: String?): ApiException {
            return ApiException(ERROR_CODE_CACHE_NULL, errorMsg, null, Kind.CACHE_NULL)
        }


        fun asApiException(throwable: Throwable): ApiException {
            // NoSuchElementException
            // JsonSyntaxException  json解析异常

            if (throwable is HttpException) {
                // We had non-200 http error
                val response = throwable.response()
                return httpError(throwable.code(), response?.raw()?.request?.url.toString(), throwable.message())
            } else return when (throwable) {
                is NoConnectivityException -> {
                    // No connection
                    noConnectivityError(throwable.message)
                }
                is IOException -> {
                    // A network error happened
                    networkError(throwable.message)
                }
                is TokenInvalidException -> {
                    // token invalid
                    tokenError(throwable.message)
                }
                is BusinessServiceException -> {
                    // business layer error
                    businessServiceError(throwable.code, throwable.message, throwable.responseJson)
                }
                else -> {
                    // We don't know what happened. We need to simply convert to an unknown error
                    unexpectedError(throwable.message)
                }
            }
        }
    }

    sealed class Kind{
        object NETWORK: Kind()
        object NO_CONNECT: Kind()
        object HTTP: Kind()
        object TOKEN: Kind()
        object BUSINESS_SERVICE: Kind()
        object UNEXPECTED: Kind()
        object CACHE_NULL: Kind()
    }

}
