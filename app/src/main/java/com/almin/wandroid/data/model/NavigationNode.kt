package com.almin.wandroid.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Almin on 2022/3/29.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class NavigationNode (var articles: List<Article>,
                      var cid: Int,
                      var name: String) : Parcelable