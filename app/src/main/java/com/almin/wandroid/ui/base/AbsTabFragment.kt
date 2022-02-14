package com.almin.wandroid.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.almin.arch.ui.AbsLazyFragment
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.Contract.PageEvent
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.ui.AppContract
import com.almin.wandroid.ui.AppViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by Almin on 2020/12/22.
 * tab base fragment
 */
abstract class AbsTabFragment<VB : ViewBinding, S: PageState, Effect: Contract.PageEffect, VM : AbstractViewModel<S,*, Effect>>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB)
    : AbsLazyFragment<VB,S,Effect,VM>(inflate){

    protected val appViewModel: AppViewModel by sharedViewModel()

    open protected fun onUserLogin(userInfo: UserInfo){
    }

    open protected fun onUserLogout(){
    }

    override fun addBackPressedCallback(): Boolean {
        return false
    }

    override fun handleOnBackPressed(): Boolean {
        return true
    }

    override fun lazyLoadData() {
        bind(Lifecycle.State.RESUMED){
            appViewModel.commonState.collect{
                when(it){
                    is AppContract.State.LoginSuccess -> {
                        onUserLogin(it.userInfo)
                    }
                    is AppContract.State.LogoutSuccess -> {
                        onUserLogout()
                    }
                    else -> {}
                }
            }
        }
    }
}