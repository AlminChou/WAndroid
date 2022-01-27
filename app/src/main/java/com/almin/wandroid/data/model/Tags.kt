package com.almin.wandroid.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
@SuppressLint("ParcelCreator")
@Parcelize
@Entity
data class Tags(var name:String, var url:String): Parcelable
