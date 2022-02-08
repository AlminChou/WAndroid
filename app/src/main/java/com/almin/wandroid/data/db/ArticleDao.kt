package com.almin.wandroid.data.db

import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.almin.wandroid.data.model.Article

/**
 * Created by Almin on 2022/1/24.
 */
@Dao
interface ArticleDao {

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveListCache(articles: List<Article>)

    @Query("SELECT * FROM article WHERE articleType =:articleType Order by publishTime desc")
    fun queryArticleCache(articleType: Int): PagingSource<Int, Article>

    @Query("DELETE FROM article WHERE articleType=:articleType")
    suspend fun clearArticleByType(articleType: Int)
}