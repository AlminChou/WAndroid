package com.almin.wandroid.ui.module.portal

import android.view.View
import androidx.fragment.app.viewModels
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.databinding.FragmentTabPortalBinding
import com.almin.wandroid.ui.base.AbsTabFragment

// 传送门 ： 广场、导航、体系
class PortalTabFragment : AbsTabFragment<FragmentTabPortalBinding, PageState, Contract.PageEffect, HolderViewModel>(FragmentTabPortalBinding::inflate) {

    override val viewModel: HolderViewModel by viewModels()

    override fun initView(rootView: View) {
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
    }

    override fun handleState(state: PageState) {
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }
}