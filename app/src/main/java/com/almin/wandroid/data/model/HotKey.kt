package com.almin.wandroid.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Almin on 2022/4/13.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class HotKey(var id: Int,
                          var link: String,
                          var name: String,
                          var order: Int,
                          var visible: Int) : Parcelable
