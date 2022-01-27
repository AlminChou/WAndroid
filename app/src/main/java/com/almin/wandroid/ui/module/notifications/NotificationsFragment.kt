package com.almin.wandroid.ui.module.notifications

import android.view.View
import androidx.fragment.app.viewModels
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.databinding.FragmentNotificationsBinding
import com.almin.wandroid.ui.base.AbsTabFragment

class NotificationsFragment : AbsTabFragment<FragmentNotificationsBinding, PageState, HolderViewModel>(FragmentNotificationsBinding::inflate) {
    override fun lazyLoadData() {
    }

    override val viewModel: HolderViewModel by viewModels()

    override fun initView(rootView: View) {
        println("11111111111111111212 viewModel ${viewModel}  ")
    }

    override fun handleState(state: PageState) {
    }

}