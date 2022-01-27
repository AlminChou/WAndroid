package com.almin.wandroid.ui.module.mine

import android.view.View
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.databinding.FragmentTabMineBinding
import com.almin.wandroid.ui.base.AbsTabFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Almin on 2022/1/8.
 */
class MineTabFragment : AbsTabFragment<FragmentTabMineBinding, Contract.PageState, HolderViewModel>(FragmentTabMineBinding::inflate) {

    override val viewModel: HolderViewModel by viewModel()

    override fun initView(rootView: View) {
        println("1111111111111111111111111123213 MineTabFragment  initView")

    }

    override fun lazyLoadData() {

        println("1111111111111111111111111123213 MineTabFragment  lazyLoadData")
    }

    override fun handleState(state: Contract.PageState) {

    }
}