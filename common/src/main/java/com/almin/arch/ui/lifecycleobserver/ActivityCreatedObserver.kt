package com.almin.arch.ui.lifecycleobserver

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface ActivityCreatedObserver : DefaultLifecycleObserver {
    fun onActivityCreated()
    override fun onCreate(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        onActivityCreated()
    }
}