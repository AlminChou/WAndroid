package com.almin.wandroid.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.almin.arch.ui.AbstractActivity
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.R
import com.almin.wandroid.databinding.ActivityMainBinding
import com.almin.wandroid.ui.navigator.AppNavigator
import com.almin.wandroid.ui.navigator.CustomFragmentNavigator
import com.almin.wandroid.ui.navigator.appNavigator
import com.almin.wandroid.ui.widget.StatusBarUtil
import com.blankj.utilcode.util.ToastUtils
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AbstractActivity<ActivityMainBinding, AppContract.State, AppContract.Effect, AppViewModel>(ActivityMainBinding::inflate) , AppNavigator {

    override val viewModel: AppViewModel by viewModel()
    private var exitTime: Long = 0L

    override fun initView() {
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        StatusBarUtil.setTranslucent(this, 100)
        setupKoinFragmentFactory()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.app_nav_host) as NavHostFragment
        navHostFragment.childFragmentManager.fragmentFactory = supportFragmentManager.fragmentFactory
        // xml fragment节点 获取方法
//        val navController = findNavController(R.id.app_nav_host)
        //FragmentContainerView 获取方法
        val navController = navHostFragment.navController
        val navigator = CustomFragmentNavigator(
            this,
            navHostFragment.childFragmentManager,
            navHostFragment.id
        )
        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.app_navigation)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            run {
                println("MainActivity     addOnDestinationChangedListener    ${destination.id}    ")
            }
//            println("MainActivity           navController.backStack.size ${navController.backStack.size}")
        }
    }

    override fun initData() {
    }

    override fun backTo(destination: Int, firstOrLast: Boolean) {
//        val option =  NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(destination, false).build()
//        Navigation.findNavController(this, R.id.app_nav_host).navigate(destination, null, option)
        //navigateUp
        Navigation.findNavController(this, R.id.app_nav_host).popBackStack(destination, false)
    }

    override fun backToAndNewPage(destination: Int, include: Boolean) {
        // popto
    }

    override fun navigate(destination: Int, type: AppNavigator.NavigationType, args: Bundle?, anim: AppNavigator.Anim) {
        val option = NavOptions.Builder()
            .setEnterAnim(anim.enter)
            .setExitAnim(anim.exit)
            .setPopEnterAnim(anim.popEnter)
            .setPopExitAnim(anim.popExit).build()
        Navigation.findNavController(this, R.id.app_nav_host)
            .navigate(destination, (args?: Bundle()).apply { putString(AppNavigator.TYPE_KEY, type.name) }, option)
    }
    override fun display(destination: Int, type: AppNavigator.NavigationType, args: Bundle?) {
        navigate(destination, AppNavigator.NavigationType.Add, args = args, AppNavigator.Anim.RightInRightOut)
    }

    override fun navigate(url: String, type: AppNavigator.NavigationType, args: Bundle?, anim: AppNavigator.Anim) {
    }

    override fun pop() {
        Navigation.findNavController(this, R.id.app_nav_host).popBackStack()
    }

    override fun exit(){
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtils.showShort("再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun handleState(state: AppContract.State) {

    }

    override fun handleEffect(effect: AppContract.Effect) {
        when(effect){
            is AppContract.Effect.Navigation2Login -> {
                navigate(R.id.navigation_login, AppNavigator.NavigationType.Add, null)
            }
        }
    }
}