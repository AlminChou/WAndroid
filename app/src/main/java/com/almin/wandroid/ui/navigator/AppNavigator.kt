package com.almin.wandroid.ui.navigator

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.almin.wandroid.R


/**
 * Created by Almin on 2020/12/28.
 */

fun Fragment.appNavigator() : AppNavigator{
    if(activity is AppNavigator){
        return activity as AppNavigator
    }else{
        throw Exception("Activity is not AppNavigator")
    }
}

interface AppNavigator {
    // fragment show type
    companion object{
        const val NAVIGATION_TYPE_ADD = "navigation_type_add"
        const val NAVIGATION_TYPE_REPLACE = "navigation_type_replace"
        const val TYPE_KEY = "fragment_show_type"
    }

    sealed class NavigationType(val name: String){
        object Add : NavigationType(NAVIGATION_TYPE_ADD)
        object Replace : NavigationType(NAVIGATION_TYPE_REPLACE)
    }

    sealed class Anim (val enter: Int, val exit: Int, val popEnter: Int, val popExit: Int){
        object FadeIn : Anim(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        object RightInRightOut : Anim(R.anim.slide_in_right_fade, android.R.anim.fade_out, android.R.anim.fade_in, R.anim.slide_out_right_fade)
        object RightInLeftOutAppend : Anim(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
    }

    //  A->B->C    ===>    C -> A
    fun backTo(@IdRes destination: Int, firstOrLast: Boolean)   // pop掉自己上面所有    firstOrLast 栈里第一个 / 最后一个  （重复对象）

    // A->B->C->D   ====> C -> A(new instance/refresh)
    fun backToAndNewPage(@IdRes destination: Int, include: Boolean) //是否包括自己

    // add replace

    // navigate 应该是 每次都新创建对象页面，  一次性返回前面页面应该用 backTo
    fun navigate(@IdRes destination: Int, type: NavigationType, args: Bundle?, anim: Anim = Anim.FadeIn)

    // 每次都新创建对象页面
    fun display(@IdRes destination: Int, type: NavigationType, args: Bundle?)

    // handle deeplink
    fun navigate(url: String, type: NavigationType, args: Bundle?, anim: Anim = Anim.FadeIn)

//    https://mp.weixin.qq.com/s/qgNbxgB-6qrFzJflqaBUdg
    // navigateUp popBackStack
    //从源码可以看出，当栈中任务大于 1 个的时候，两个函数没什么区别。当栈中只有一个导航首页 (start destination) 的时候，navigateUp() 不会弹出导航首页，
    // 它什么都不做，直接返回 false。popBackStack 则会把导航首页也出栈，但是由于没有回退到任何其他页面，
    // 此时 popBackStack 会返回 false, 如果此时又继续调用 navigate() 函数，会发生 exception。
    // 所以 google 官网说不建议把导航首页也出栈。如果导航首页出栈了，此时需要关闭当前 Activity。或者跳转到其他导航页面。
    // 示例代码如下

//    if (!navController.popBackStack()) {
//         Call finish() on your Activity
//        finish();
//    }
//    navigateUp
    fun pop()
    fun exit()
}