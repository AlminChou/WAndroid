package com.almin.wandroid.data.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.UserInfo

/**
 * Created by Almin on 2022/1/22.
 */
@Database(entities = [UserInfo::class, Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    val user: UserDao by lazy { getUserDao() }
    abstract fun getUserDao() : UserDao

    val article: ArticleDao by lazy { getArticleDao() }
    abstract fun getArticleDao() : ArticleDao

    companion object{
        fun build(context: Context) : AppDataBase{
            return Room.databaseBuilder(context, AppDataBase::class.java, "app_db")
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                    }
                })
                .build()
        }
    }
}
