package com.almin.wandroid.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.almin.arch.ktx.collect
import com.almin.arch.ui.AbstractFragment
import com.almin.arch.ui.data.PageResult
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.ui.AppContract
import com.almin.wandroid.ui.AppViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by Almin on 2022/1/21.
 * 基础UI
 */
abstract class AbsFragment<VB : ViewBinding, S : Contract.PageState, Effect : Contract.PageEffect, VM : AbstractViewModel<S, *, Effect>>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : AbstractFragment<VB, S, Effect, VM>(inflate) {

    protected val appViewModel: AppViewModel by activityViewModel()

    open protected fun onUserLogin(userInfo: UserInfo) {

    }

    open protected fun onUserLogout() {

    }

    override fun initData() {
        appViewModel.commonState.collect(viewLifecycleOwner, Lifecycle.State.CREATED) {
            when (it) {
                is AppContract.State.LoginSuccess -> {
                    onUserLogin(it.userInfo)
                }
                is AppContract.State.LogoutSuccess -> {
                    onUserLogout()
                }
                else -> {}
            }
        }

        appViewModel.pageResult.collect(viewLifecycleOwner, Lifecycle.State.CREATED) {
            onFragmentResult(it)
        }
    }

    /**
     * 页面通讯result回调接收, 原生的setFragmentResult 只能在同一个fragmentManager作用域，暂时先用这个方式
     */
    open protected fun onFragmentResult(pageResult: PageResult) {

    }

    fun setResult(pageResult: PageResult) {
        appViewModel.postPageResult(pageResult)
    }
}