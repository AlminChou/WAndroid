package com.almin.arch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract.PageEvent
import com.almin.arch.viewmodel.Contract.PageState


/**
 * Created by Almin on 2019-07-18.
 * 懒加载fragment, 日后用 FragmentTransaction#setMaxLifecycle(Fragment, Lifecycle.State) 重新实现
 */
abstract class AbsLazyFragment<VB : ViewBinding, S: PageState, VM : AbstractViewModel<S,*>>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) : AbstractFragment<VB,S,VM>(inflate) {

    /**
     * 是否可见状态 为了避免和[Fragment.isVisible]冲突 换个名字
     */
    private var isFragmentVisible: Boolean = false

    /**
     * 是否第一次加载
     */
    private var isFirstLoad = true

    /**
     * 是否完成初始化view， false则已经被回收destroy
     */
    private var isViewInitiated: Boolean = false

    /**
     * <pre>
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     * 一般用于PagerAdapter需要刷新各个子Fragment的场景
     * 不要new 新的 PagerAdapter 而采取reset数据的方式
     * 所以要求Fragment重新走initData方法
     * 故使用 [setForceLoad]来让Fragment下次执行initData
    </pre> *
     */
    public var forceLoad = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isFirstLoad = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewInitiated = true
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            onVisible()
            lazyLoad()
        } else {
            onInvisible()
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     * eg:
     * transaction.hide(aFragment);
     * transaction.show(aFragment);
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     * visible.
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onVisible()
            lazyLoad()
        } else {
            onInvisible()
        }
    }

    @CallSuper
    protected open fun onVisible() {
        isFragmentVisible = true
    }

    @CallSuper
    protected open fun onInvisible() {
        isFragmentVisible = false
    }

    final override fun initData() {
        lazyLoad()
    }

    protected fun lazyLoad() {
        if (isViewInitiated && isFragmentVisible) {
            if (forceLoad || isFirstLoad) {
                forceLoad = false
                isFirstLoad = false
                lazyLoadData()
            }
        }
    }

    /**
     * 实现正常流程下的加载数据操作
     */
    protected abstract fun lazyLoadData()

    /**
     *  强制重新加载数据，若界面可见直接刷新，否则修改flag待下次可见再刷新
     */
    public fun refreshData(){
        if(isFragmentVisible && isViewInitiated){
            lazyLoadData()
        }else{
            forceLoad = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewInitiated = false
    }

    protected fun isPageVisible() = isFragmentVisible

    protected fun isViewInitiated() = isViewInitiated
}