package com.almin.wandroid.data.db

import androidx.room.TypeConverter
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Tags
import com.almin.wandroid.data.model.UserInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


/**
 * Created by Almin on 2022/1/24.
 */
class Converters {


    @TypeConverter
    fun tagsJsonToList(value: String): List<Tags>? {
        val adapter: JsonAdapter<List<Tags>> = adapter()
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun tagsListToJson(value: List<Tags>): String {
        val adapter: JsonAdapter<List<Tags>> = adapter()
        return adapter.toJson(value)
    }


    @TypeConverter
    fun articleList(value: List<Article>): String {
        val adapter: JsonAdapter<List<Article>> = adapter()
        return adapter.toJson(value)
    }

    @TypeConverter
    fun listArticle(value: String): List<Article>? {
        val adapter: JsonAdapter<List<Article>> = adapter()
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromList(value : List<String>): String {
        val adapter: JsonAdapter<List<String>> = adapter()
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toList(value: String) : List<String>? {
        val adapter: JsonAdapter<List<String>> = adapter()
        return adapter.fromJson(value)
    }

    inline fun <reified T : Any> adapter() : JsonAdapter<T>  {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(type())
    }

    inline fun <reified T : Any> type() =
        T::class.java

     fun < T> toJson(value: List<T>) : String{
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(type())
        return adapter.toJson(value)
    }

}
