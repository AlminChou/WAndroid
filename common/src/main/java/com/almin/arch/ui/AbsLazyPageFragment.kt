package com.almin.arch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract.PageEffect
import com.almin.arch.viewmodel.Contract.PageState

// viewpage2 fragment
abstract class AbsLazyPageFragment<VB : ViewBinding, S: PageState, Effect: PageEffect, VM : AbstractViewModel<S,*, Effect>>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) : AbstractFragment<VB,S,Effect,VM>(inflate) {

    // 一般都是子fragment 不需要添加
    override fun addBackPressedCallback(): Boolean {
        return false
    }

    /**
     * 是否第一次加载
     */
    private var isFirstLoad = true

    final override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            lazyLoadData()
            isFirstLoad = false
        }
    }

    /**
     * 实现正常流程下的加载数据操作
     */
    protected abstract fun lazyLoadData()

    override fun onDestroyView() {
        super.onDestroyView()
        isFirstLoad = false
    }
}