package com.almin.wandroid.ui.module

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.almin.arch.ui.AbstractFragment
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.R
import com.almin.wandroid.databinding.FragmentSplashBinding
import com.almin.wandroid.ui.base.AbsFragment
import com.almin.wandroid.ui.navigator.AppNavigator
import com.almin.wandroid.ui.navigator.appNavigator
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Almin on 2020/12/24.
 */
class SplashFragment : AbsFragment<FragmentSplashBinding, PageState, Contract.PageEffect, HolderViewModel>(FragmentSplashBinding::inflate){

    override val viewModel: HolderViewModel by viewModel()

    override fun initView(rootView: View) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            delay(1500)
            //            val navController = Navigation.findNavController(activity as AppCompatActivity, R.id.app_nav_host)
//            navController.popBackStack(R.id.navigation_splash, true)
//            navController.navigate(R.id.action_navigation_splash_to_navigation_main)
            appNavigator().navigate(R.id.action_navigation_splash_to_navigation_main, AppNavigator.NavigationType.Add, null)
        }
    }

    override fun initData() {
    }

    override fun handleState(state: PageState) {
    }

    override fun handleOnBackPressed(): Boolean {
        (activity as AppNavigator).exit()
        return true
    }

    override fun handleEffect(effect: Contract.PageEffect) {

    }
}