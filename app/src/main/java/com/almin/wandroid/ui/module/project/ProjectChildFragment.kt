package com.almin.wandroid.ui.module.project

import android.graphics.Rect
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.almin.arch.ui.AbsLazyPageFragment
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.R
import com.almin.wandroid.const.Key.BUNDLE_KEY_PROJECT_CATEGORY_ID
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.databinding.FragmentTabProjectChildBinding
import com.almin.wandroid.ui.module.common.ArticleAdapter
import com.almin.wandroid.ui.base.singleItemClick
import com.almin.wandroid.ui.navigator.AppNavigator
import com.almin.wandroid.ui.navigator.appNavigator
import com.blankj.utilcode.util.LogUtils
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Created by Almin on 2022/2/15.
 */
class ProjectChildFragment : AbsLazyPageFragment<FragmentTabProjectChildBinding, ProjectContract.PageState, Contract.PageEffect, ProjectViewModel>(FragmentTabProjectChildBinding::inflate) {

    companion object{
        fun instance(categoryId: Int) : ProjectChildFragment{
            val fragment = ProjectChildFragment()
            fragment.arguments = bundleOf(BUNDLE_KEY_PROJECT_CATEGORY_ID to categoryId)
            return fragment
        }
    }

    override val viewModel: ProjectViewModel by viewModel()
    private lateinit var adapter: ArticleAdapter

    override fun initView(rootView: View) {
        binding.rvProject.layoutManager = LinearLayoutManager(rootView.context)
        adapter = ArticleAdapter(mutableListOf())
        binding.rvProject.adapter = adapter
        adapter.loadMoreModule.isEnableLoadMore = true
        binding.rvProject.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val childPosition = parent.getChildAdapterPosition(view)
                outRect.top = 15
                if (childPosition == 0 || childPosition == parent.adapter!!.itemCount - 1 ) {
                    outRect.bottom = 15
                } else {
                    outRect.bottom = 0
                }
            }
        })
        adapter.loadMoreModule.isAutoLoadMore = true
        adapter.loadMoreModule.setOnLoadMoreListener { //上拉加载时取消下拉刷新
            viewModel.setEvent(ProjectContract.PageEvent.LoadProjectListByTab(false))
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.setEvent(ProjectContract.PageEvent.LoadProjectListByTab(true))
        }
        adapter.singleItemClick{ adapter, view, position ->
            val item: Article = adapter.data[position] as Article
            appNavigator().display(
                R.id.navigation_web,
                AppNavigator.NavigationType.Add, bundleOf("url" to item.link, "title" to item.title)
            )
        }
    }

    override fun lazyLoadData() {
        viewModel.setEvent(ProjectContract.PageEvent.LoadProjectListByTab(true))
    }

    override fun handleState(state: ProjectContract.PageState) {
        when(state.loadStatus){
            LoadStatus.LoadMore -> {
                binding.refreshLayout.isRefreshing = false
                adapter.loadMoreModule.loadMoreToLoading()
            }
            LoadStatus.LoadMoreFailed -> {
                adapter.loadMoreModule.loadMoreFail()
            }
            LoadStatus.LoadMoreFinish -> {
                adapter.loadMoreModule.loadMoreComplete()
                state.projects?.let { adapter.addData(it) }
            }
            LoadStatus.LoadFailed ->{
                adapter.loadMoreModule.isEnableLoadMore = false
                binding.refreshLayout.isRefreshing = false
                binding.viewPageState.root.isVisible = true
            }
            LoadStatus.Refresh -> {
                adapter.loadMoreModule.isEnableLoadMore = false
                binding.refreshLayout.isRefreshing = true
                binding.viewPageState.root.isVisible = false
            }
            LoadStatus.Finish -> {
                adapter.loadMoreModule.isEnableLoadMore = true
                binding.refreshLayout.isRefreshing = false
                adapter.setNewInstance(state.projects?.toMutableList())
            }
        }
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }

}