package com.almin.wandroid.ui.module.portal

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
import com.almin.wandroid.const.Key
import com.almin.wandroid.const.Key.BUNDLE_KEY_TAG_PAGE_TYPE
import com.almin.wandroid.data.model.TAG_TYPE_ARTICLE
import com.almin.wandroid.data.model.TAG_TYPE_CATEGORY
import com.almin.wandroid.data.model.TagTree
import com.almin.wandroid.databinding.FragmentCommonRefreshListBinding
import com.almin.wandroid.ui.module.portal.adapter.TagAdapter
import com.almin.wandroid.ui.navigator.AppNavigator
import com.almin.wandroid.ui.navigator.appNavigator
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Almin on 2022/3/18.
 * 体系、导航 标签入口
 */
class TagFragment : AbsLazyPageFragment<FragmentCommonRefreshListBinding, PortalContract.PageState, Contract.PageEffect, PortalViewModel>(
    FragmentCommonRefreshListBinding::inflate) {

    companion object{
        fun newInstance(type: Int): TagFragment {
            val fragment = TagFragment()
            fragment.arguments = bundleOf(BUNDLE_KEY_TAG_PAGE_TYPE to type)
            return fragment
        }
    }


    override val viewModel: PortalViewModel by viewModel()
    private lateinit var adapter: TagAdapter

    override fun initView(rootView: View) {
        binding.rvProject.layoutManager = LinearLayoutManager(rootView.context)
        adapter = TagAdapter(emptyList<TagTree>().toMutableList())
        binding.rvProject.adapter = adapter
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
            viewModel.setEvent(PortalContract.PageEvent.LoadTagTree(true))
        }
//        adapter.singleItemClick{ childAdapter, view, position ->
//            if(childAdapter.data[position] is TagNode){
//                val node = (childAdapter.data[position] as TagNode)
//                when(node.type){
//                    TAG_TYPE_CATEGORY -> {
////                            appNavigator().display(
////                                R.id.navigation_result_list,
////                                AppNavigator.NavigationType.Add,
////                                bundleOf().apply {
////                                    putParcelable(Key.BUNDLE_KEY_CATEGORY_TAG_NODE, this)
////                                })
//                    }
//                    TAG_TYPE_ARTICLE -> {
//                        node.article?.run {
//                            appNavigator().display(
//                                R.id.navigation_web,
//                                AppNavigator.NavigationType.Add, bundleOf("url" to link, "title" to title)
//                            )
//                        }
//                    }
//                }
//            }
//        }

        adapter.setOnTagClickListener{ tagTree, tagNode, position ->
            when(tagNode.type){
                    TAG_TYPE_CATEGORY -> {
                            appNavigator().display(
                                R.id.navigation_result_list,
                                AppNavigator.NavigationType.Add,
                                bundleOf(Key.BUNDLE_KEY_POSITION to position).apply {
                                    putParcelable(Key.BUNDLE_KEY_CATEGORY_TAG_NODE, tagTree)
                                })
//                        println("12312321321321312   1 1   ${tagTree.groupName}")
                    }
                    TAG_TYPE_ARTICLE -> {
                        tagNode.article?.run {
                            appNavigator().display(
                                R.id.navigation_web,
                                AppNavigator.NavigationType.Add, bundleOf("url" to link, "title" to title)
                            )
                        }
                    }
                }
        }
    }

    override fun lazyLoadData() {
        viewModel.setEvent(PortalContract.PageEvent.LoadTagTree(false))
    }

    override fun handleState(state: PortalContract.PageState) {
        when(state.loadStatus){
            LoadStatus.RefreshFailed ->{
                binding.refreshLayout.isRefreshing = false
                binding.viewPageState.root.isVisible = true
            }
            LoadStatus.Refresh -> {
                binding.refreshLayout.isRefreshing = true
                binding.viewPageState.root.isVisible = false
            }
            LoadStatus.RefreshFinish -> {
                binding.refreshLayout.isRefreshing = false
                adapter.setNewInstance(state.tagTree?.toMutableList())
            }
        }
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }

}