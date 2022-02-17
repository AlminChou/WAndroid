package com.almin.wandroid.ui.module.register

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.almin.arch.ui.AbstractFragment
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.R
import com.almin.wandroid.databinding.FragmentSigninBinding
import com.almin.wandroid.ui.base.AbsFragment
import com.almin.wandroid.ui.navigator.appNavigator
import com.almin.wandroid.ui.widget.StatusBarUtil
import com.blankj.utilcode.util.KeyboardUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Almin on 2022/1/6.
 */
class RegisterFragment : AbsFragment<FragmentSigninBinding, RegisterContract.State, Contract.PageEffect, RegisterViewModel>(FragmentSigninBinding::inflate) {

    override val viewModel: RegisterViewModel by viewModel()

    override fun initView(rootView: View) {
        StatusBarUtil.setAppbarTopPadding(binding.appbarLayout.root)
        (activity as AppCompatActivity).run {
            setSupportActionBar(binding.appbarLayout.toolbar)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.appbarLayout.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.appbarLayout.toolbar.title = "注册"


        binding.btnSignIn.setOnClickListener {
            viewModel.setEvent(RegisterContract.Event.ClickSignIn(
                binding.etUsername.text.toString().trim(),
                binding.etPwd.text.toString().trim(),
                binding.etPwdConfirm.text.toString().trim()))
        }
    }

    override fun initData() {
    }

    override fun handleState(state: RegisterContract.State) {
        when(state.loadStatus){
            LoadStatus.Loading -> {
                KeyboardUtils.hideSoftInput(activity)
                binding.pbLoading.isVisible = true
            }
            LoadStatus.Finish -> {
                binding.pbLoading.isVisible = false
                appNavigator().backTo(R.id.navigation_main, false)
            }
            else -> binding.pbLoading.isVisible = false
        }
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }
}