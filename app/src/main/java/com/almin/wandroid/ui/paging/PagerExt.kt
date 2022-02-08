package com.almin.wandroid.ui.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.almin.arch.repository.Repository
import com.almin.wandroid.data.model.RemoteKeys
import com.almin.wandroid.utils.NetworkUtil
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Almin on 2022/1/17.
 */

fun <Value : Any> ViewModel.pager(config: PagingConfig, initialKey: Int, apiCall: suspend (pageIndex: Int) -> List<Value>) : Flow<PagingData<Value>> {
    return Pager(config = config, initialKey = initialKey){
        object : PagingSource<Int, Value>(){
            override fun getRefreshKey(state: PagingState<Int, Value>): Int {
                return initialKey
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
                return try {
                    val currentPage = params.key ?: 0
                    val data = apiCall(currentPage)
                    val prevKey = if (currentPage == 0) null else currentPage - 1
                    val nextKey = if(data.isNullOrEmpty() || data.size < config.pageSize) null else currentPage + 1
//                    delay(5000)
                    LoadResult.Page(data = data, prevKey = prevKey, nextKey = nextKey)
                }catch (e: Exception){
                    LoadResult.Error(e)
                }
            }
        }
    }.flow.cachedIn(viewModelScope)
}



@OptIn(ExperimentalPagingApi::class)
fun <Value : Any> Repository.remoteMediatorPage(config: PagingConfig,
                                                     pagingSourceFactory: () -> PagingSource<Int, Value>,
                                                 keyQuery: suspend (lastItem: Value) -> RemoteKeys?,
                                                 apiCall: suspend (pageIndex: Int) -> List<Value>, db: RoomDatabase,
                                                 dbClean: suspend (db: RoomDatabase) -> Unit,
                                                 dbInsert: suspend (db: RoomDatabase, prevKey: Int?, nextKey: Int?, data: List<Value>) -> Unit): Flow<PagingData<Value>> {
    return Pager(config = config,
        remoteMediator = remoteMediator(config = config, apiCall = apiCall, db = db, dbClean = dbClean, dbInsert = dbInsert, keyQuery = keyQuery),
        pagingSourceFactory = pagingSourceFactory).flow
}


/**
 * 参考 https://developer.android.com/codelabs/android-paging#19
 */
@OptIn(ExperimentalPagingApi::class)
fun <Value : Any> Repository.remoteMediator(config: PagingConfig,
                                                 keyQuery: suspend (lastItem: Value) -> RemoteKeys?,
                                                 apiCall: suspend (pageIndex: Int) -> List<Value>, db: RoomDatabase,
                                                 dbClean: suspend (db: RoomDatabase) -> Unit,
                                                 dbInsert: suspend (db: RoomDatabase, prevKey: Int?, nextKey: Int?, data: List<Value>) -> Unit)
    : RemoteMediator<Int, Value> {

    return object : RemoteMediator<Int, Value>(){
        override suspend fun load(loadType: LoadType, state: PagingState<Int, Value>): MediatorResult {
            try {
                // 判断状态
                val pageKey = when (loadType) {
                    LoadType.REFRESH -> null  // 初次请求 或者 page.refresh
                    LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                    LoadType.APPEND -> { // 下来加载更多时触发
                        val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                        val remoteKeys = keyQuery(lastItem)
                        if(remoteKeys?.nextKey == null){
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        remoteKeys.nextKey
                    }
                }

                if (!NetworkUtil.isNetworkAvailable()) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                // 请求数据
                val page = pageKey ?: 0
                val data = apiCall(page)
                val endOfPaginationReached = data.isNullOrEmpty()

                // 插入数据
                db.withTransaction {
                    // clear all tables in the database
                    if (loadType == LoadType.REFRESH) {
                        dbClean(db)
                    }

                    val prevKey = if (page == 0) null else page - 1
                    val nextKey = if(endOfPaginationReached || data.size < config.pageSize) null else page + 1
                    dbInsert(db, prevKey, nextKey, data)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (e: IOException) {
                return MediatorResult.Error(e)
            } catch (e: HttpException) {
                return MediatorResult.Error(e)
            }
        }
    }
}