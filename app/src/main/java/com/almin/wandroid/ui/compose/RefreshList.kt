package com.almin.wandroid.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Almin on 2022/1/18.
 */

@Composable
fun RefreshLazyColumn(firstInit: Boolean, isRefreshing: Boolean, pager: LazyPagingItems<*>, refresh: ()-> Unit, listContent: LazyListScope.(CoroutineScope, LazyListState) -> Unit){
    val refreshState = rememberSwipeRefreshState(isRefreshing)

    //  multi api + page api
    refreshState.isRefreshing = refreshState.isRefreshing && (pager.loadState.refresh is LoadState.Loading)


//    if(){
//        FullScreenLoading()
//        return
//    }

    SwipeRefresh(
        state = refreshState,
        onRefresh = refresh,
    ){
        Feed(refreshState, pager, listContent = listContent)
    }
}

@Composable
fun Feed(refreshState: SwipeRefreshState, pager: LazyPagingItems<*>, listContent: LazyListScope.(CoroutineScope, LazyListState) -> Unit) {
    val listState =  rememberLazyListState() //LazyListState() else
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(state = listState) {
        /// data content
        listContent(coroutineScope, listState)

        if(refreshState.isRefreshing){
            return@LazyColumn
        }

        pager.loadState.run {
            when {
                refresh is LoadState.Loading -> {
                }
                refresh is LoadState.Error -> {
                    val e = refresh as LoadState.Error
                    item{ LoadMoreError{ pager.retry() } }
                }
                append is LoadState.Loading -> {
                    item { LoadMoreLoading() }
                }
                append is LoadState.Error -> {
//                        val e = append as LoadState.Error
//                        e.error.localizedMessage
                    item{ LoadMoreError{ pager.retry() } }
                }
                append is LoadState.NotLoading -> {
                    if(append.endOfPaginationReached) item{ LoadMoreEnd() }
                }
            }
        }
    }
}

@Composable
fun LoadMoreLoading(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFFF77B7A),
            modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
        )
    }
}

@Composable
fun LoadMoreError(retry: () -> Unit){
    Box(
        Modifier
            .fillMaxWidth()
            .padding(20.dp), contentAlignment = Alignment.Center){
        Text(modifier = Modifier.clickable { retry() }, text = "重试", fontSize = 16.sp)
    }
}

@Composable
fun LoadMoreEnd(){
    Box(
        Modifier
            .fillMaxWidth()
            .padding(20.dp), contentAlignment = Alignment.Center){
        Text(text = "没有更多了", fontSize = 16.sp)
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}