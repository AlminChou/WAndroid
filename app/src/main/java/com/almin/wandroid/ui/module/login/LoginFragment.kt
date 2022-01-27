package com.almin.wandroid.ui.module.login

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.almin.arch.ui.AbstractFragment
import com.almin.wandroid.R
import com.almin.wandroid.databinding.FragmentLoginBinding
import com.almin.wandroid.ui.AppViewModel
import com.almin.wandroid.ui.navigator.AppNavigator
import com.almin.wandroid.ui.widget.StatusBarUtil
import com.blankj.utilcode.util.KeyboardUtils
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Almin on 2022/1/4.
 */
class LoginFragment : AbstractFragment<FragmentLoginBinding, LoginContract.State, LoginViewModel>(FragmentLoginBinding::inflate) {
    private val appViewModel: AppViewModel by sharedViewModel()
    override val viewModel: LoginViewModel by viewModel()

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
        binding.appbarLayout.toolbar.title = "登录"

        binding.btnLogin.setOnClickListener {
            viewModel.setEvent(LoginContract.Event.ClickLogin(binding.etUsername.text.toString().trim(), binding.etPwd.text.toString().trim()))
        }
        binding.pbLoading.setOnClickListener {

        }
        binding.tvSignIn.setOnClickListener {
            (activity as AppNavigator).display(R.id.navigation_register,
                AppNavigator.NavigationType.Add, null)
        }
    }

    override fun initData() {

    }

    override fun handleState(state: LoginContract.State) {
        when(state){
            LoginContract.State.Logining -> {
                KeyboardUtils.hideSoftInput(activity)
                binding.pbLoading.isVisible = true
            }
            LoginContract.State.LoginFailed -> {
                binding.pbLoading.isVisible = false
            }
            is LoginContract.State.LoginSuccess -> {
                binding.pbLoading.isVisible = false
            }
        }
    }

}