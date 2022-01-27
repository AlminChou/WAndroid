package com.almin.wandroid.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Entity
data class UserInfo(
    val admin: Boolean,
    val chapterTops: List<String>?,
    val coinCount: Int,
    val collectIds: List<String>?,
    val email: String?,
    val icon: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nickname: String?,
    val password: String?,
    val publicName: String?,
    val token: String?,
    val type: Int,
    val username: String?
) : Parcelable