package com.almin.arch.repository

data class Resource<out T>(val status: Status, val data: T?, val cache: T?, val throwable: Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.Success, data, null, null)
        }

        fun <T> error(throwable: Throwable?): Resource<T> {
            return Resource(Status.Error, null, null, throwable)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.Loading, data, null, null)
        }

        fun <T> cache(cache: T?): Resource<T> {
            return Resource(Status.Cache, null, cache, null)
        }
    }
}

sealed class Status{
    object Success: Status()
    object Error: Status()
    object Loading: Status()
    object Cache: Status()
}

