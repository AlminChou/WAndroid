package com.almin.newtemplate.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

/**
 * Created by Almin on 2020/12/24.
 */

class AppFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return super.instantiate(classLoader, className)
    }
}