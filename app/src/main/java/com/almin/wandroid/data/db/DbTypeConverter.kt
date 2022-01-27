package com.almin.wandroid.data.db

import androidx.room.TypeConverter
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Tags
import com.almin.wandroid.data.model.UserInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types




/**
 * Created by Almin on 2022/1/24.
 */
class Converters {


    @TypeConverter
    fun tagsJsonToList(value: String): List<Tags>? {
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Tags>> = moshi.adapter(
            Types.newParameterizedType(
                List::class.java,
                Tags::class.java
            ))
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun tagsListToJson(value: List<Tags>): String {
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Tags>> = moshi.adapter(
            Types.newParameterizedType(
                List::class.java,
                Tags::class.java
            ))
        return adapter.toJson(value)
    }

    @TypeConverter
    fun articleJsonToList(value: String): List<Article>? {
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Article>> = moshi.adapter(
            Types.newParameterizedType(
                List::class.java,
                Article::class.java
            ))
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun ArticleListToJson(value: List<Article>): String {
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Article>> = moshi.adapter(
            Types.newParameterizedType(
                List::class.java,
                Article::class.java
            ))
        return adapter.toJson(value)
    }

//    @TypeConverter
//    fun stringToObject(value: String): UserInfo? {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(UserInfo::class.java)
//        return adapter.fromJson(value)
//    }
//
//    @TypeConverter
//    fun objectToString(userInfo: UserInfo): String {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(UserInfo::class.java)
//        return adapter.toJson(userInfo)
//    }
//
    @TypeConverter
    fun fromList(value : List<String>): String {
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<String>> = moshi.adapter(
            Types.newParameterizedType(
                List::class.java,
                String::class.java
            ))
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toList(value: String) : List<String>? {
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<String>> = moshi.adapter(
            Types.newParameterizedType(
                List::class.java,
                String::class.java
            ))
        return adapter.fromJson(value)
    }

    inline fun <reified T : Any> type() =
        T::class.java

//    @TypeConverter
//     fun < T> toJson(value: T) : String{
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(type())
//        return adapter.toJson(value)
//    }
//
//    @TypeConverter
//     fun < T> fromJson(value: String) : Any? {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(type())
//        return adapter.fromJson(value)
//    }
//
//    @TypeConverter
//     fun < T> toJson(value: List<T>) : String{
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(type())
//        return adapter.toJson(value)
//    }

}
