package com.almin.wandroid.ui.navigator

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

/**
 * Created by Almin on 2020/12/22.
 * fix tab fragment auto create logic, custom handle fragment navigate need
 */
@Navigator.Name("tab_fragment")
class TabFragmentNavigator: FragmentNavigator {
    private lateinit var manager: FragmentManager
    private lateinit var context: Context
    private var containerId: Int = -1

    constructor(context: Context, manager: FragmentManager, containerId: Int) : super(
        context,
        manager,
        containerId
    ){
        this.manager = manager
        this.context = context
        this.containerId = containerId
    }


    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            Log.i(
                "Tab Navigator", "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }

        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()

        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)
        println("fragmentfragment   ${fragment == null}  ${tag}")
//
//        //反射获取mBackStack mIsPendingBackStackOperation
//        val backStackField = FragmentNavigator::class.java.getDeclaredField("mBackStack")
//        backStackField.isAccessible = true
//        val backStack: ArrayDeque<Int> = backStackField.get(this) as ArrayDeque<Int>
//
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        if (fragment == null) {
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            fragment.arguments = args
            transaction.add(containerId, fragment, tag)
//            transaction.addToBackStack(tag)
        } else {
//            backStack.clear()
            transaction.show(fragment)
        }
//        backStack.add(destination.id)

        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        transaction.commitNow()

//        return if (initialNavigate) {
            return destination
//        } else {
//            null
//        }
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }

    class TabNavhostFragment : NavHostFragment(){
        override fun createFragmentNavigator(): Navigator<out Destination> {
            return TabFragmentNavigator(requireContext(), childFragmentManager, id)
        }

    }
}
