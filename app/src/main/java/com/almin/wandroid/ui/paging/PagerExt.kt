package com.almin.wandroid.ui.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.Flow

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