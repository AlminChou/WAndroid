package com.almin.wandroid.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.almin.wandroid.App

/**
 * Created by Almin on 2022/1/8.
 */
object NetworkUtil {

    fun isNetworkAvailable(): Boolean {
            try {
                val connectivityManager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } catch (e: Exception) {
                return false
            }
        }

}