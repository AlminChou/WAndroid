package com.almin.wandroid.ui.module.search

import android.view.View
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.databinding.FragmentSearchBinding
import com.almin.wandroid.ui.base.AbsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Almin on 2022/4/13.
 */
class SearchFragment : AbsFragment<FragmentSearchBinding, SearchContract.PageState, Contract.PageEffect, SearchViewModel>(FragmentSearchBinding::inflate) {

    override val viewModel: SearchViewModel by viewModel()

    override fun initView(rootView: View) {
        binding.cvSearch.setContent {
            MaterialTheme{
                LazyColumn{
                    item {
                        HotKeyList()
                    }
                }
            }
        }
    }

    @Composable
    fun HotKeyList(){

    }

    override fun initData() {
        super.initData()
    }

    override fun handleState(state: SearchContract.PageState) {
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }
}