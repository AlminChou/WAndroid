package com.almin.wandroid.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.almin.arch.ui.AbstractFragment
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract

/**
 * Created by Almin on 2022/1/21.
 * 基础UI
 */
abstract class AbsFragment<VB : ViewBinding, S: Contract.PageState, VM : AbstractViewModel<S,*>>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) : AbstractFragment<VB, S, VM>(inflate) {



}