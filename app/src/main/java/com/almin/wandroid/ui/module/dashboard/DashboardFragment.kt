package com.almin.wandroid.ui.module.dashboard

import android.view.View
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.databinding.FragmentDashboardBinding
import com.almin.wandroid.ui.base.AbsTabFragment
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class DashboardFragment : AbsTabFragment<FragmentDashboardBinding, PageState, HolderViewModel>(FragmentDashboardBinding::inflate) {

    override fun lazyLoadData() {
    }

    override val viewModel: HolderViewModel by sharedStateViewModel()

    override fun initView(rootView: View) {
        println("11111111111111111212 viewModel ${viewModel}  ")

    }

    override fun handleState(state: PageState) {
    }

}