package com.almin.wandroid.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Entity
data class Article(
    var apkLink: String = "",
    var author: String,//作者
    var chapterId: Int,
    var chapterName: String = "",
    var collect: Boolean,//是否收藏
    var courseId: Int,
    var desc: String = "",
    var envelopePic: String = "",
    var fresh: Boolean,
    @PrimaryKey
    var id: Int,
    var link: String = "",
    var niceDate: String = "",
    var origin: String = "",
    var prefix: String = "",
    var projectLink: String = "",
    var publishTime: Long,
    var superChapterId: Int,
    var superChapterName: String = "",
    var shareUser: String,
    var tags: List<Tags> = emptyList(),
    var title: String = "",
    var type: Int,
    var userId: Int,
    var visible: Int,
    var zan: Int,
    var articleType: Int = -1) : Parcelable {


    val isTop: Boolean
        get() {
            return type == 1
        }
    val isNew: Boolean
        get(){
            return fresh
        }
}
