package com.almin.wandroid.data.db

import androidx.annotation.WorkerThread
import androidx.room.*
import com.almin.wandroid.data.model.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao //@JvmSuppressWildcards
interface UserDao {

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userInfo: UserInfo)

    @Query("SELECT * FROM userinfo")
    fun loadUserInfo() : Flow<List<UserInfo>>

    @Query("SELECT * FROM userinfo")
    suspend fun allUserInfo() : List<UserInfo>

    @Delete
    suspend fun delete(userInfo: UserInfo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userInfo: UserInfo)
}