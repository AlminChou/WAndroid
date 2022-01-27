package com.almin.wandroid.ui.module.main

import android.util.ArrayMap
import android.view.View
import com.almin.arch.ui.AbstractFragment
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.R
import com.almin.wandroid.databinding.FragmentMainBinding
import com.almin.wandroid.ui.base.AbsTabFragment
import com.almin.wandroid.ui.module.dashboard.DashboardFragment
import com.almin.wandroid.ui.module.home.HomeFragment
import com.almin.wandroid.ui.module.mine.MineTabFragment
import com.almin.wandroid.ui.module.notifications.NotificationsFragment
import com.almin.wandroid.ui.navigator.AppNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by Almin on 2020/12/24.
 * can replace with TabFragmentNavigator
 */
class MainFragment : AbstractFragment<FragmentMainBinding, PageState, HolderViewModel>(FragmentMainBinding::inflate){

    override val viewModel: HolderViewModel by sharedViewModel()
    private lateinit var navView: BottomNavigationView
    private lateinit var tabFragments: ArrayMap<Int, AbsTabFragment<*,*,*>>

//    SavedStateHandle

    override fun initView(rootView: View) {
//        setAppbarTopPadding(rootView as ViewGroup?, R.id.appbar)

        childFragmentManager.fragmentFactory = activity?.supportFragmentManager!!.fragmentFactory
        navView = rootView.findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener {
            if (it.isChecked) return@setOnItemSelectedListener false
            navigate(it.itemId, false)
            return@setOnItemSelectedListener  true
        }
        navView.setOnItemReselectedListener {

        }
        initTabPage()
    }

    private fun initTabPage() {
        tabFragments = ArrayMap()
        tabFragments[R.id.navigation_home] = HomeFragment()
        tabFragments[R.id.navigation_dashboard] = DashboardFragment()
        tabFragments[R.id.navigation_notifications] = NotificationsFragment()
        tabFragments[R.id.navigation_mine] = MineTabFragment()

        navigate(R.id.navigation_home, true)
        navigate(R.id.navigation_notifications, true)
        navigate(R.id.navigation_dashboard, true)
        navigate(R.id.navigation_mine, true)

        // default
        navigate(R.id.navigation_home, false)
    }

    override fun initData() {
    }

    private fun navigate(fragmentId: Int, init: Boolean){
        val transaction = childFragmentManager.beginTransaction()
        val tabFragment = tabFragments[fragmentId]!!
        if(init && !tabFragment.isAdded){
            transaction.add(R.id.nav_host_fragment, tabFragment, tabFragment.tag).hide(tabFragment)
        }else{
            childFragmentManager.primaryNavigationFragment?.run {
                transaction.hide(this)
            }
            transaction.show(tabFragment)
        }
        transaction.setPrimaryNavigationFragment(tabFragment)
        transaction.commit()
    }

    override fun handleOnBackPressed(): Boolean {
        if(childFragmentManager.primaryNavigationFragment == null) return false

        println("  hildFragmentManager.primaryNavigationFragment  ${childFragmentManager.primaryNavigationFragment}")
        when(childFragmentManager.primaryNavigationFragment){
            is HomeFragment -> {
                (activity as AppNavigator).exit()
            }
            else -> {
                navView.selectedItemId = R.id.navigation_home
            }
        }
        return true
    }

    override fun handleState(state: PageState) {
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}