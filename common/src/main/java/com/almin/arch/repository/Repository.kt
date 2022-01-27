package com.almin.arch.repository

import kotlinx.coroutines.flow.*

/**
 * Created by Almin on 2022/1/26.
 */
abstract class Repository {


    sealed class Category{
        object CacheFirstFetchCover : Category() // 优先展示缓存 ，请求覆盖
        object FetchFailedCacheCover : Category() // 请求失败后，读取缓存覆盖
        object QueryAfterFetchSave: Category()  //  请求成功后写入缓存，读取缓存进行覆盖
    }

    inline fun <T> networkBoundResource(crossinline cacheQuery: () -> Flow<T>,
                                    crossinline fetch: suspend () -> T,
                                    crossinline shouldFetch: (T?) -> Boolean,
                                    crossinline saveFetchResult: suspend (T) -> Unit,
                                    crossinline onFetchFailed:(Throwable) -> Unit, category: Category = Category.FetchFailedCacheCover
    ): Flow<Resource<T>> = flow {
        emit(Resource.loading(null))

        val cacheValue = try {
            cacheQuery().first()
        }catch (throwable: Throwable) {
            emitAll(emptyFlow<Resource<T>>().map { Resource.error(throwable) })
            null
        }

        when(category){
            Category.CacheFirstFetchCover -> {
                emit(Resource.cache(cacheValue))
                if (shouldFetch(cacheValue)){
                    try {
                        val fetchData = fetch()
                        saveFetchResult(fetchData)
                        emit(Resource.success(fetchData))
                    } catch (throwable: Throwable) {
                        onFetchFailed(throwable)
                        emit(Resource.error(throwable))
                    }
                }
            }

            Category.FetchFailedCacheCover -> {
                if (shouldFetch(cacheValue)){
                    try {
                        val fetchData = fetch()
                        saveFetchResult(fetchData)
                        emit(Resource.success(fetchData))
                    } catch (throwable: Throwable) {
                        onFetchFailed(throwable)
                        emit(Resource.cache(cacheValue))
                        emit(Resource.error(throwable))
                    }
                }
            }

            Category.QueryAfterFetchSave -> {
                if (shouldFetch(cacheValue)) {
                    emit(Resource.loading(cacheValue))
                    try {
                        saveFetchResult(fetch())
                        emitAll(cacheQuery().map { Resource.success(it) })
                    } catch (throwable: Throwable) {
                        onFetchFailed(throwable)
                        emitAll(cacheQuery().map { Resource.error(throwable) })
                    }
                } else {
                    emitAll(cacheQuery().map { Resource.success(it) })
                }
            }
        }

    }
}