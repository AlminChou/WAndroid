package com.almin.wandroid.middleware

import android.content.Context
import com.almin.arch.middleware.ResourceProvider

/**
 * Created by Almin on 2022/1/8.
 */
class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    override fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }

    override fun getString(resourceId: Int, vararg args: Any): String {
        return if (args.isNotEmpty()) {
            context.resources.getString(resourceId, *args)
        } else {
            context.resources.getString(resourceId)
        }
    }
}