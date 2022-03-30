package com.almin.wandroid.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Almin on 2022/2/16.
 */
@Parcelize
data class ProjectCategory(var children: List<String> = listOf(),
                           var courseId: Int = 0,
                           var id: Int = 0,
                           var name: String = "",
                           var order: Int = 0,
                           var parentChapterId: Int = 0,
                           var userControlSetTop: Boolean = false,
                           var visible: Int = 0) : Parcelable

data class ProjectTabInfo(var titleList: List<String>, val category: List<ProjectCategory>)