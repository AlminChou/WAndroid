package com.almin.wandroid.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.almin.wandroid.data.model.Article

/**
 * Created by Almin on 2022/1/24.
 */
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveHomeListCache(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCollectListCache(articles: List<Article>)


}