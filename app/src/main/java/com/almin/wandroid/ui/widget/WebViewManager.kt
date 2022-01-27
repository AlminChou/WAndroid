package com.almin.wandroid.ui.widget

import android.content.Context
import android.content.Intent
import android.content.MutableContextWrapper
import android.net.Uri
import android.os.Looper
import android.view.ViewGroup
import android.webkit.*
import com.almin.wandroid.utils.NetworkUtil


class WebViewManager private constructor() {

    private val webViewCache: MutableList<WebView> = ArrayList(1)

    private fun create(context: Context): WebView {
        val webView = WebView(context)
        webView.settings.javaScriptEnabled = true
        //设置自适应屏幕，两者合用
        webView.settings.useWideViewPort = true//将图片调整到适合webView的大小
        webView.settings.loadWithOverviewMode = true// 缩放至屏幕的大小
        webView.settings.defaultTextEncodingName = "UTF-8"
        WebView.setWebContentsDebuggingEnabled(true)
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.cacheMode = if(NetworkUtil.isNetworkAvailable()) WebSettings.LOAD_DEFAULT else WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webView.settings.domStorageEnabled = true // 开启 DOM storage API 功能
        webView.settings.setAppCacheEnabled(true)//开启 Application Caches 功能
        //加载https和http混合
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.webChromeClient = object : WebChromeClient() {

        }
        webView.webViewClient = WebViewClient()


        return webView
    }

    fun prepare(context: Context) {
        if (webViewCache.isEmpty()) {
            Looper.myQueue().addIdleHandler {
                webViewCache.add(create(MutableContextWrapper(context)))
                false
            }
        }
    }

    fun obtain(context: Context): WebView {
        if (webViewCache.isEmpty()) {
            webViewCache.add(create(MutableContextWrapper(context)))
        }
        val webView = webViewCache.removeFirst()
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        webView.clearHistory()
        webView.resumeTimers()
        return webView
    }

    fun recycle(webView: WebView) {
        try {
            webView.stopLoading()
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            webView.pauseTimers()
            webView.webChromeClient = null
//            webView.webViewClient = null
            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (!webViewCache.contains(webView)) {
                webViewCache.add(webView)
            }
        }
    }

    fun destroy() {
        try {
            webViewCache.forEach {
                it.removeAllViews()
                it.destroy()
                webViewCache.remove(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun canGoBack(webView: WebView): Boolean {
        val canBack = webView.canGoBack()
        if (canBack) webView.goBack()
        val backForwardList = webView.copyBackForwardList()
        val currentIndex = backForwardList.currentIndex
        if (currentIndex == 0) {
            val currentUrl = backForwardList.currentItem?.url
            val currentHost = Uri.parse(currentUrl).host
            //栈底不是链接则直接返回
            if (currentUrl.isNullOrEmpty() || currentHost.isNullOrBlank()) return false
        }
        return canBack
    }
}