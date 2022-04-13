package com.almin.wandroid.ui.module.portal

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.const.Key
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.TagTree
import com.almin.wandroid.databinding.FragmentSystemArticleBinding
import com.almin.wandroid.ui.base.AbsFragment
import com.almin.wandroid.ui.module.common.ArticleFeedFragment
import com.almin.wandroid.ui.widget.StatusBarUtil
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Almin on 2022/4/11.
 * 搜索结果列表，体系类型结果列表
 */
class SystemArticleListFragment : AbsFragment<FragmentSystemArticleBinding, Contract.PageState, Contract.PageEffect, HolderViewModel>(FragmentSystemArticleBinding::inflate) {

    override val viewModel: HolderViewModel by viewModel()

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

        arguments?.run {
            val node: TagTree? = getParcelable(Key.BUNDLE_KEY_CATEGORY_TAG_NODE)
            node?.run {
                binding.toolbar.title = groupName
                val currentTagPosition = getInt(Key.BUNDLE_KEY_POSITION)
                val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    tab.text = list?.get(position)?.name
                }
                binding.viewPager.adapter = object : FragmentStateAdapter(this@SystemArticleListFragment){
                    override fun getItemCount(): Int = list?.size?:0
                    override fun createFragment(position: Int): Fragment {
                        return ArticleFeedFragment.instance(Article.ARTICLE_TYPE_SYSTEM_LIST, list!![position].projectCategory?.id?:0)
                    }
                }
                tabLayoutMediator.attach()
                binding.viewPager.postDelayed({
                    binding.viewPager.setCurrentItem(currentTagPosition, true)
                }, 500)
            }
        }
    }

    override fun handleState(state: Contract.PageState) {
    }

    override fun handleEffect(effect: Contract.PageEffect) {

    }
}