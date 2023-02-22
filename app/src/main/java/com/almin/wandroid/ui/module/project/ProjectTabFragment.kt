package com.almin.wandroid.ui.module.project

import android.view.View
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.databinding.FragmentTabProjectBinding
import com.almin.wandroid.ui.base.AbsTabFragment
import com.almin.wandroid.ui.base.singleClick
import com.almin.wandroid.ui.module.project.adapter.ProjectTabAdapter
import com.almin.wandroid.ui.widget.StatusBarUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProjectTabFragment : AbsTabFragment<FragmentTabProjectBinding, ProjectContract.PageState, Contract.PageEffect, ProjectViewModel>(FragmentTabProjectBinding::inflate) {

    override val viewModel: ProjectViewModel by viewModel()

    override fun initView(rootView: View) {
        StatusBarUtil.setAppbarTopPadding(binding.appbarLayout)

        binding.vpProject.offscreenPageLimit = 1
        binding.vpProject.isUserInputEnabled = true
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.vpProject.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {}
        })

        binding.viewPageState.tvRetry.singleClick {
            viewModel.setEvent(ProjectContract.PageEvent.LoadCategoryTab)
        }
    }

    override fun lazyLoadData() {
        viewModel.setEvent(ProjectContract.PageEvent.LoadCategoryTab)
    }

    override fun handleState(state: ProjectContract.PageState) {
        when(state.loadStatus){
            LoadStatus.Finish -> {
                if(binding.vpProject.adapter == null){
                    val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.vpProject) { tab, position ->
                        tab.text = state.tabInfo!!.titleList[position]
                    }
                    binding.vpProject.adapter = ProjectTabAdapter(this, state.tabInfo!!)
                    tabLayoutMediator.attach()
                    binding.viewPageState.root.isVisible = false
                }
            }
            LoadStatus.LoadFailed -> {
                binding.viewPageState.root.isVisible = true
                binding.viewPageState.ivError.isVisible = true
                binding.viewPageState.loadingview.isVisible = false
                binding.viewPageState.tvRetry.isVisible = true
            }
            LoadStatus.Loading -> {
                binding.viewPageState.root.isVisible = true
                binding.viewPageState.loadingview.isVisible = true
                binding.viewPageState.ivError.isVisible = false
                binding.viewPageState.tvRetry.isVisible = false
            }
            else -> { }
        }
    }

    override fun handleEffect(effect: Contract.PageEffect) {

    }

}