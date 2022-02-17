package com.almin.wandroid.ui.module.project

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.almin.arch.ui.AbsLazyPageFragment
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.databinding.FragmentTabProjectChildBinding
import com.almin.wandroid.ui.base.ArticleAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Created by Almin on 2022/2/15.
 */
class ProjectChildFragment : AbsLazyPageFragment<FragmentTabProjectChildBinding, ProjectContract.PageState, Contract.PageEffect, ProjectViewModel>(FragmentTabProjectChildBinding::inflate) {

    override val viewModel: ProjectViewModel by viewModel()

    override fun initView(rootView: View) {
        binding.rvProject.layoutManager = LinearLayoutManager(rootView.context)
        binding.rvProject.adapter = ArticleAdapter(mutableListOf())
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
        binding.refreshLayout.setOnRefreshListener {
            viewModel.setEvent(ProjectContract.PageEvent.LoadProjectListByTab)
        }
    }

    override fun lazyLoadData() {
        viewModel.setEvent(ProjectContract.PageEvent.LoadProjectListByTab)
    }

    override fun handleState(state: ProjectContract.PageState) {
        when(state.loadStatus){
            LoadStatus.Finish -> {
                (binding.rvProject.adapter as ArticleAdapter).setNewInstance(state.projects?.toMutableList())
            }
        }
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }

}