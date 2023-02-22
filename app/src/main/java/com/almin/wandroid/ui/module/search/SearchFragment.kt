package com.almin.wandroid.ui.module.search

import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.HotKey
import com.almin.wandroid.databinding.FragmentSearchBinding
import com.almin.wandroid.ui.base.AbsFragment
import com.almin.wandroid.ui.widget.StatusBarUtil
import com.google.accompanist.flowlayout.FlowRow
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.almin.wandroid.R


/**
 * Created by Almin on 2022/4/13.
 */
class SearchFragment : AbsFragment<FragmentSearchBinding,
        SearchContract.PageState,
        Contract.PageEffect, SearchViewModel>(FragmentSearchBinding::inflate) {

    override val viewModel: SearchViewModel by viewModel()

    override fun initView(rootView: View) {
        StatusBarUtil.setAppbarTopPadding(binding.appbar)
        (activity as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.cvSearch.setContent {
            MaterialTheme {
                LazyColumn {
                    item {
                        HotKeyList(viewModel.pageState.hotKeys)
                        HistoryList(viewModel.pageState.history)
                    }
                }
            }
        }
    }

    @Composable
    fun HistoryList(history: List<String>?) {
        Column{
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(top = 12.dp, start = 6.dp, bottom = 6.dp),
                        text = "搜索历史",
                        style = TextStyle(color = Color(0xff279dee), fontSize = 15.sp)
                    )

                    Text(
                        modifier = Modifier.padding(top = 12.dp, start = 6.dp, bottom = 6.dp, end = 10.dp).clickable {
                            viewModel.setEvent(SearchContract.PageEvent.CleanHistory)
                        },
                        text = "清空",
                        style = TextStyle(color = Color(0xff333333), fontSize = 15.sp)
                    )
                }
            history?.run {
                forEach {
                    Row{
                        SearchLabel(it)
                    }
                }
            }
        }
    }

    @Composable
    fun HotKeyList(hotKeys: List<HotKey>?) {
        Text(
            modifier = Modifier.padding(top = 12.dp, start = 6.dp, bottom = 6.dp),
            text = "热门搜索",
            style = TextStyle(color = Color(0xff279dee), fontSize = 15.sp)
        )
        if (!hotKeys.isNullOrEmpty()) {
            FlowRow {
                hotKeys.forEach {
                    SearchLabel(it.name)
                }
            }
        }
    }

    @Composable
    fun SearchLabel(text: String){
        Text(
            modifier = Modifier
                .padding(6.dp)
                .background(
                    color = Color(0xFFF6F6F6),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(8.dp, 5.dp, 8.dp, 5.dp),
            text = text,
            style = TextStyle(color = Color(0xff8d8d8e), fontSize = 12.sp)
        )
    }

    override fun initData() {
//        super.initData()
        viewModel.setEvent(SearchContract.PageEvent.Refresh)
    }

    override fun handleState(state: SearchContract.PageState) {
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}