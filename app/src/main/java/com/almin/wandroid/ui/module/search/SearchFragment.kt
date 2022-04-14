package com.almin.wandroid.ui.module.search

import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.HotKey
import com.almin.wandroid.databinding.FragmentSearchBinding
import com.almin.wandroid.ui.base.AbsFragment
import com.google.accompanist.flowlayout.FlowRow
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
                        HotKeyList(viewModel.pageState.hotKeys)
                    }
                }
            }
        }
    }

    @Composable
    fun HotKeyList(hotKeys: List<HotKey>?){
        if(!hotKeys.isNullOrEmpty()){
            FlowRow {
                hotKeys.forEach {
                    Text(modifier = Modifier.padding(6.dp), text = it.name, color = Color.Black)
                }
            }
        }else{

        }
    }

    override fun initData() {
//        super.initData()
        viewModel.setEvent(SearchContract.PageEvent.Refresh)
    }

    override fun handleState(state: SearchContract.PageState) {
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }
}