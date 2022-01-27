package com.almin.arch.repository

import kotlinx.coroutines.flow.*


//https://stackoverflow.com/questions/58486364/networkboundresource-with-kotlin-coroutines
//https://stackoverflow.com/questions/65980152/emitall-until-another-operation-is-done/65984833#65984833
/**
 * Created by Almin on 2022/1/26.
 */
abstract class NetworkBoundResource<T> {

    //    fun asFlow(): Flow<Resource<T>> = flow {
    inline fun networkBoundResource(crossinline query: () -> Flow<T>,
                      crossinline fetch: () -> T,
                      crossinline shouldFetch: (T) -> Boolean,
                      crossinline saveFetchResult:(T) -> Unit,
                      crossinline onFetchFailed:(Throwable) -> Unit
    ): Flow<Resource<T>> = flow {
        // flatMapConcat implementation
//        val flow = query()
//            .onStart { emit(Resource.loading<T>(null)) }
//            .flatMapConcat { data ->
//                // cache first
//                emit(Resource.loading(data))
//                // api call
//                if (shouldFetch(data)) {
//                    try {
//                        saveFetchResult(fetch())
//                        query().map { Resource.success(it) }
//                    } catch (throwable: Throwable) {
//                        onFetchFailed(throwable)
//                        query().map { Resource.error(throwable.localizedMessage, it) }
//                    }
//                } else {
//                    query().map { Resource.success(it) }
//                }
//            }
//
//        emitAll(flow)

        // doesn't use the flatMapConcat
        emit(Resource.loading(null))

        val dbValue = query().first()

        if (shouldFetch(dbValue)) {
            emit(Resource.loading(dbValue))
            try {
                saveFetchResult(fetch())
                emitAll(query().map { Resource.success(it) })
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                emitAll(query().map { Resource.error(throwable) })
            }
        } else {
            emitAll(query().map { Resource.success(it) })
        }
    }

//    abstract fun query(): Flow<T>
//    abstract suspend fun fetch(): T
//    abstract suspend fun saveFetchResult(data: T)
//    open fun onFetchFailed(throwable: Throwable) = Unit
//    open fun shouldFetch(data: T) = true
}