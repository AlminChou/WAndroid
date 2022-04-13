package com.almin.wandroid.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Almin on 2022/3/29.
 */

const val TAG_TYPE_CATEGORY = 1
const val TAG_TYPE_ARTICLE = 2

@Parcelize
data class TagTree(val groupName: String, val list: List<TagNode>?): Parcelable

@Parcelize
data class TagNode(val name: String?, val id: Int?, val article: Article?,
                   val projectCategory: ProjectCategory?, val type: Int = TAG_TYPE_CATEGORY) : Parcelable