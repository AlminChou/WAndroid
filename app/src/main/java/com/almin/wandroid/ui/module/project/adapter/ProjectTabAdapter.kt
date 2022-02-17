package com.almin.wandroid.ui.module.project.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.almin.wandroid.data.model.ProjectTabInfo
import com.almin.wandroid.ui.module.project.ProjectChildFragment

/**
 * Created by Almin on 2022/2/17.
 */
class ProjectTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

    private lateinit var tabInfo: ProjectTabInfo
    private lateinit var fragments: List<ProjectChildFragment>


    constructor(fragment: Fragment,  tabInfo: ProjectTabInfo)  : this(fragment = fragment){
        this.tabInfo = tabInfo
        fragments = tabInfo.category.map {
            ProjectChildFragment()
        }
    }

    override fun getItemCount(): Int = tabInfo.titleList.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}