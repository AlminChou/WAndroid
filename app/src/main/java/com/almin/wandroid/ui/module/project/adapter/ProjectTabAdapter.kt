package com.almin.wandroid.ui.module.project.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.almin.wandroid.data.model.ProjectCategory
import com.almin.wandroid.data.model.ProjectTabInfo
import com.almin.wandroid.ui.module.project.ProjectChildFragment

/**
 * Created by Almin on 2022/2/17.
 */
class ProjectTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

    private lateinit var tabInfo: ProjectTabInfo
    private lateinit var fragments: MutableList<ProjectChildFragment>


    constructor(fragment: Fragment,  tabInfo: ProjectTabInfo)  : this(fragment = fragment){
        this.tabInfo = tabInfo
        val titleList = tabInfo.titleList.toMutableList()
        titleList.add(0, "最新")
        tabInfo.titleList = titleList
        fragments = tabInfo.category.map {
            ProjectChildFragment.instance(it.id)
        }.toMutableList()
        //最新
        fragments.add(0, ProjectChildFragment.instance(0))
    }

    override fun getItemCount(): Int = tabInfo.titleList.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}