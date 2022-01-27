package com.almin.wandroid.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.almin.arch.ui.AbsLazyFragment
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract.PageEvent
import com.almin.arch.viewmodel.Contract.PageState

/**
 * Created by Almin on 2020/12/22.
 * tab base fragment
 */
abstract class AbsTabFragment<VB : ViewBinding, S: PageState, VM : AbstractViewModel<S,*>>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB)
    : AbsLazyFragment<VB,S,VM>(inflate){

    override fun addBackPressedCallback(): Boolean {
        return false
    }

    override fun handleOnBackPressed(): Boolean {
        return true
    }
}