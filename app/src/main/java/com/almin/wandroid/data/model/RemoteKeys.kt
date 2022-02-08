package com.almin.wandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    val prevKey: Int?,
    @PrimaryKey
    val articleType: Int,
    val nextKey: Int?
)