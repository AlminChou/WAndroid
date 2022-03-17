package com.almin.wandroid.ui.module.mine

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.almin.wandroid.R
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.databinding.FragmentTabMineBinding
import com.almin.wandroid.ui.AppContract
import com.almin.wandroid.ui.base.AbsTabFragment
import com.almin.wandroid.ui.base.ConfirmFragmentDialog
import com.almin.wandroid.ui.base.singleClick
import com.almin.wandroid.ui.navigator.AppNavigator
import com.almin.wandroid.ui.navigator.appNavigator
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Created by Almin on 2022/1/8.
 */
class MineTabFragment : AbsTabFragment<FragmentTabMineBinding, MineTabContract.PageState, MineTabContract.PageEffect, MineTabViewModel>(FragmentTabMineBinding::inflate) {

    override val viewModel: MineTabViewModel by viewModel()

    override fun initView(rootView: View) {
        binding.tvLogin.singleClick{
            appViewModel.setEvent(AppContract.Event.Login)
        }
        binding.clLogout.singleClick {
            viewModel.setEvent(MineTabContract.PageEvent.Logout)
        }
        binding.clWanPage.singleClick {
            viewModel.setEvent(MineTabContract.PageEvent.ClickWanLink)
        }
        binding.clIntegral.singleClick {
            viewModel.setEvent(MineTabContract.PageEvent.ClickIntegralRule)
        }
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        binding.ivAvatar.load("https://www.wanandroid.com/resources/image/pc/logo.png"){
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    override fun onUserLogin(userInfo: UserInfo) {
        binding.clLogout.isVisible = true
        binding.tvUsername.isVisible = true
        binding.tvEmail.isVisible = true
        binding.tvLogin.isVisible = false
        binding.tvUsername.text = userInfo.username
        binding.tvEmail.text = userInfo.email
    }

    override fun onUserLogout() {
        binding.clLogout.isVisible = false
        binding.tvUsername.visibility = View.INVISIBLE
        binding.tvEmail.visibility = View.INVISIBLE
        binding.tvLogin.isVisible = true
    }

    override fun handleState(state: MineTabContract.PageState) {

    }

    override fun handleEffect(effect: MineTabContract.PageEffect) {
        when(effect){
            is MineTabContract.PageEffect.AlertLogoutDialog -> {
                val confirmFragmentDialog: ConfirmFragmentDialog =
                    ConfirmFragmentDialog.instance(
                        "提示",
                        "是否退出登录",
                        "取消", "确认"
                    )
                confirmFragmentDialog.onDialogClickListener = object : ConfirmFragmentDialog.OnDialogClickListener{
                    override fun onConfirm() {
                        appViewModel.setEvent(AppContract.Event.Logout)
                    }

                    override fun onCancel() {
                    }
                }
                confirmFragmentDialog.show(childFragmentManager)
            }
            is MineTabContract.PageEffect.Navigation2WanPage -> {
                appNavigator().display(R.id.navigation_web, AppNavigator.NavigationType.Add,
                    bundleOf("url" to "https://www.wanandroid.com", "title" to "玩Android"))
            }

            is MineTabContract.PageEffect.Navigation2IntegralRulePage -> {
                appNavigator().display(R.id.navigation_web, AppNavigator.NavigationType.Add,
                    bundleOf("url" to "https://www.wanandroid.com/blog/show/2653", "title" to "积分规则"))
            }

            is MineTabContract.PageEffect.Navigation2SettingPage -> {}
        }
    }
}