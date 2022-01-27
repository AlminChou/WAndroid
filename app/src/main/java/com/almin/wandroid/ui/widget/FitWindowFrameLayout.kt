package com.almin.wandroid.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout

/**
 * Created by Almin on 2020/12/29.
 */
class FitWindowFrameLayout : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        setOnHierarchyChangeListener(object : OnHierarchyChangeListener{
            override fun onChildViewAdded(parent: View?, child: View?) {
                requestApplyInsets()
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {

            }

        })
    }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        for (i in 0 until childCount){
            getChildAt(i).dispatchApplyWindowInsets(insets)
        }
//        return super.onApplyWindowInsets(insets)
        return insets!!
    }
}
