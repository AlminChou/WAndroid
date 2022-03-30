package com.almin.wandroid.ui.module.portal

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.databinding.FragmentTabPortalBinding
import com.almin.wandroid.ui.base.AbsTabFragment
import com.almin.wandroid.ui.module.portal.PortalContract.Companion.TYPE_NAVIGATION
import com.almin.wandroid.ui.module.portal.PortalContract.Companion.TYPE_SYSTEM
import com.almin.wandroid.ui.widget.StatusBarUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

// 传送门 ： 广场、导航、体系
class PortalTabFragment : AbsTabFragment<FragmentTabPortalBinding, PageState, Contract.PageEffect, HolderViewModel>(FragmentTabPortalBinding::inflate) {

    override val viewModel: HolderViewModel by viewModel()

    override fun initView(rootView: View) {
        StatusBarUtil.setAppbarTopPadding(binding.appbarLayout)

        binding.vpProtal.offscreenPageLimit = 1
        binding.vpProtal.isUserInputEnabled = true
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.vpProtal.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {}
        })

        val tabName = listOf("广场", "体系", "导航")

        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.vpProtal) { tab, position ->
            tab.text = tabName[position]
        }
        val fragments = listOf<Fragment>(SquareFragment(), TagFragment.newInstance(TYPE_SYSTEM), TagFragment.newInstance(TYPE_NAVIGATION))
        binding.vpProtal.adapter = PortalTabAdapter(this, fragments)
        tabLayoutMediator.attach()
    }

    override fun lazyLoadData() {

    }

    override fun handleState(state: PageState) {
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }


    inner class PortalTabAdapter(fragment: Fragment) :  FragmentStateAdapter(fragment){
        private lateinit var fragments: List<Fragment>

        constructor(fragment: Fragment, fragments: List<Fragment>) : this(fragment = fragment){
            this.fragments = fragments
        }

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}