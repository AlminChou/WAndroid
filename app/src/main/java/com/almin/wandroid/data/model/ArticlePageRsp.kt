package com.almin.wandroid.data.model

/**
 * Created by Almin on 2022/1/17.
 */
data class ArticlePageRsp(
    val curPage: Int,
    val datas: List<Article> = emptyList(),
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)
