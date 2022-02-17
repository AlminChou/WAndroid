package com.almin.wandroid.data.model

data class PagerResponse<T>(
    var datas: List<T>,
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)  {
    fun isRefresh() = offset == 0

    fun noMore() = over
}