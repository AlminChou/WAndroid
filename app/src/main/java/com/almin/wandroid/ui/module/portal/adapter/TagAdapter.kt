package com.almin.wandroid.ui.module.portal.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.almin.wandroid.R
import com.almin.wandroid.data.model.TagNode
import com.almin.wandroid.data.model.TagTree
import com.almin.wandroid.ui.base.singleItemClick
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


/**
 * Created by Almin on 2022/4/2.
 */
class TagAdapter(tagTree: MutableList<TagTree>?) : BaseQuickAdapter<TagTree, BaseViewHolder>(R.layout.list_item_portal_tag, tagTree) {


    override fun convert(holder: BaseViewHolder, item: TagTree) {
        holder.setText(R.id.tv_group_name, item.groupName)
        val rv = holder.getView<RecyclerView>(R.id.rv_tree)

        if(rv.layoutManager == null){
            val layoutManager = FlexboxLayoutManager(holder.itemView.context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.flexWrap = FlexWrap.WRAP
            rv.isNestedScrollingEnabled = false
            rv.layoutManager = layoutManager
        }
        val data = item.list?.toMutableList()?:mutableListOf()
        if(rv.adapter == null){
            rv.adapter = TagNodeAdapter(data)
        }else{
            (rv.adapter as TagNodeAdapter).setNewInstance(data)
        }
        (rv.adapter as TagNodeAdapter).singleItemClick{childAdapter, view, position ->
            onClick?.invoke(item, childAdapter.data[position] as TagNode, position)
        }
    }

    private var onClick: ((tagTree: TagTree, tagNode: TagNode, position: Int) -> Unit) ? = null

    fun setOnTagClickListener(onClick: ((tagTree: TagTree, tagNode: TagNode, position: Int) -> Unit)){
        this.onClick = onClick
    }

    private class TagNodeAdapter(tagNode: MutableList<TagNode>?) : BaseQuickAdapter<TagNode, BaseViewHolder>(R.layout.list_item_portal_tag_node, tagNode){

        override fun convert(holder: BaseViewHolder, item: TagNode) {
            val tvName = holder.getView<TextView>(R.id.tv_node_name)
            tvName.text = item.name
            if(holder.itemView.tag  != null){
                holder.setTextColor(R.id.tv_node_name, holder.itemView.tag as Int)
            }else{
                val color = ColorUtils.getRandomColor(false)
                holder.setTextColor(R.id.tv_node_name, color)
                holder.itemView.tag = color
            }
        }
    }

}

