package com.almin.wandroid.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Entity
data class Banner(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) : Parcelable