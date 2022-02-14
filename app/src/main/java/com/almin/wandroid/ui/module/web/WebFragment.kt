package com.almin.wandroid.ui.module.web

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.almin.arch.ui.AbstractFragment
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.R
import com.almin.wandroid.databinding.FragmentWebBinding
import com.almin.wandroid.ui.widget.StatusBarUtil
import com.almin.wandroid.utils.NetworkUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Almin on 2022/1/20.
 */
class WebFragment : AbstractFragment<FragmentWebBinding, Contract.PageState, Contract.PageEffect,  HolderViewModel>(FragmentWebBinding::inflate){

    override val viewModel: HolderViewModel by viewModel()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.web_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun initView(rootView: View) {
        StatusBarUtil.setAppbarTopPadding(binding.appbarLayout.root)

        (activity as AppCompatActivity).run {
            setSupportActionBar(binding.appbarLayout.toolbar)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.appbarLayout.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.appbarLayout.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_collect -> {

                }
                R.id.action_share -> {

                }
                else -> {}
            }
            true
        }

        binding.webview.settings.javaScriptEnabled = true
        //设置自适应屏幕，两者合用
        binding.webview.settings.useWideViewPort = true//将图片调整到适合webView的大小
        binding.webview.settings.loadWithOverviewMode = true// 缩放至屏幕的大小
        binding.webview.settings.defaultTextEncodingName = "UTF-8"
        WebView.setWebContentsDebuggingEnabled(true)
        binding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.webview.settings.cacheMode = if(NetworkUtil.isNetworkAvailable()) WebSettings.LOAD_DEFAULT else WebSettings.LOAD_CACHE_ELSE_NETWORK
        binding.webview.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        binding.webview.settings.domStorageEnabled = true // 开启 DOM storage API 功能
        binding.webview.settings.setAppCacheEnabled(true)//开启 Application Caches 功能
        //加载https和http混合
        binding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.webview.webChromeClient = object : WebChromeClient() {
        }
        binding.webview.webViewClient = WebViewClient()


        arguments?.run {
            getString("url")?.let { binding.webview.loadUrl(it) }
            getString("title")?.let { binding.appbarLayout.toolbar.title = it }
        }


        // putParcelable / getParcelable
    }

    override fun handleState(state: Contract.PageState) {
    }

    override fun initData() {
    }

    override fun handleEffect(effect: Contract.PageEffect) {

    }

}