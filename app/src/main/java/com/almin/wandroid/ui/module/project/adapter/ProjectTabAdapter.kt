package com.almin.wandroid.ui.module.project.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.ProjectTabInfo
import com.almin.wandroid.ui.module.common.ArticleFeedFragment

/**
 * Created by Almin on 2022/2/17.
 */
class ProjectTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

    private lateinit var tabInfo: ProjectTabInfo


    constructor(fragment: Fragment,  tabInfo: ProjectTabInfo)  : this(fragment = fragment){
        this.tabInfo = tabInfo
        val titleList = tabInfo.titleList.toMutableList()
        titleList.add(0, "最新")
        tabInfo.titleList = titleList
    }

    override fun getItemCount(): Int = tabInfo.titleList.size

    override fun createFragment(position: Int): Fragment {
        if(position != 0){
            return ArticleFeedFragment.instance(Article.ARTICLE_TYPE_PROJECT, tabInfo.category.get(position - 1).id)
        }

        return ArticleFeedFragment.instance(Article.ARTICLE_TYPE_PROJECT, 0)
    }

}