package com.almin.wandroid.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@SuppressLint("ParcelCreator")
@Parcelize
data class Tags(var name:String, var url:String): Parcelable
