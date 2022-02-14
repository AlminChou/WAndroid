package com.almin.wandroid.ui.module.notifications

import android.view.View
import androidx.fragment.app.viewModels
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.databinding.FragmentNotificationsBinding
import com.almin.wandroid.ui.base.AbsTabFragment

class NotificationsFragment : AbsTabFragment<FragmentNotificationsBinding, PageState, Contract.PageEffect, HolderViewModel>(FragmentNotificationsBinding::inflate) {
    override fun lazyLoadData() {
            super.lazyLoadData()
    }

    override val viewModel: HolderViewModel by viewModels()

    override fun initView(rootView: View) {
    }

    override fun handleState(state: PageState) {
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }

}