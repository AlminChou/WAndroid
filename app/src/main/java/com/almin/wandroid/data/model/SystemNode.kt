package com.almin.wandroid.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Almin on 2022/3/29.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class SystemNode(var children: List<ProjectCategory>,
                          var courseId: Int,
                          var id: Int,
                          var name: String,
                          var order: Int,
                          var parentChapterId: Int,
                          var userControlSetTop: Boolean,
                          var visible: Int) : Parcelable
