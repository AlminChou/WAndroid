package com.almin.wandroid.data.model

/**
 * Created by Almin on 2022/3/29.
 */
data class TagTree(val groupName: String, val list: List<TagNode>?)

data class TagNode(val name: String, val id: Int, val article: Article)