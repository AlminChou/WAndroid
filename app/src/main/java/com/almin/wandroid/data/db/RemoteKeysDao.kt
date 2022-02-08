package com.almin.wandroid.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.almin.wandroid.data.model.RemoteKeys

/**
 * Created by Almin on 2022/2/7.
 */
@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKeys: RemoteKeys)

    @Query("SELECT * FROM remotekeys where articleType = :articleType")
    suspend fun getRemoteKeys(articleType: Int): RemoteKeys?

    @Query("DELETE FROM remotekeys where articleType = :articleType")
    suspend fun clearRemoteKeys(articleType: Int)
}