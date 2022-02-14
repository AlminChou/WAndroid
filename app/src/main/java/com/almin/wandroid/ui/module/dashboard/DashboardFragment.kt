package com.almin.wandroid.ui.module.dashboard

import android.view.View
import androidx.lifecycle.Lifecycle
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.databinding.FragmentDashboardBinding
import com.almin.wandroid.ui.AppViewModel
import com.almin.wandroid.ui.base.AbsTabFragment
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DashboardFragment : AbsTabFragment<FragmentDashboardBinding, PageState, Contract.PageEffect, HolderViewModel>(FragmentDashboardBinding::inflate) {

    override fun lazyLoadData() {
        super.lazyLoadData()
    }

    override val viewModel: HolderViewModel by sharedStateViewModel()

    override fun initView(rootView: View) {

    }

    override fun handleState(state: PageState) {
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }

}