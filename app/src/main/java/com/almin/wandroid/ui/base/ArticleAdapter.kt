package com.almin.wandroid.ui.base

import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import coil.load
import com.almin.wandroid.R
import com.almin.wandroid.data.model.Article
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by Almin on 2022/2/17.
 */
class ArticleAdapter(data: MutableList<Article>?) : BaseMultiItemQuickAdapter<Article, BaseViewHolder>(data) {

    init {
        addItemType(Article.ARTICLE_TYPE_PROJECT, R.layout.list_item_project)
    }

    override fun convert(holder: BaseViewHolder, item: Article) {
        when(holder.itemViewType){
            Article.ARTICLE_TYPE_PROJECT -> bindProject(holder, item)
        }
    }

    private fun bindProject(holder: BaseViewHolder, item: Article) {
        item.run {
            holder.setText(R.id.tv_author, author.ifEmpty { shareUser })
            holder.setText(R.id.tv_title, title.toHtml())
            holder.setText(R.id.tv_desc, desc.toHtml())
            holder.setText(R.id.tv_time, niceDate)
            holder.getView<ImageView>(R.id.iv_project).load(envelopePic){

            }
        }
        holder.getView<ImageView>(R.id.iv_collect)
    }

    fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(this, flag)
        } else {
            Html.fromHtml(this)
        }
    }

}