package com.almin.arch.network.ssl

import android.annotation.SuppressLint
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by Almin on 2018/7/6.
 */
class TrustAllSSLSocketFactory {

    companion object {
//        @JvmStatic
        fun newInstance(): SSLSocketFactory {
            val trustAllCertsManager = TrustAllCertsManager()
            val trustManager = arrayOf(trustAllCertsManager)
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManager, java.security.SecureRandom())
            return sslContext.socketFactory
        }
    }

    @SuppressLint("TrustAllX509TrustManager")
    internal class TrustAllCertsManager : X509TrustManager{
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            //do nothing
        }

        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            //do nothing
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

    }
}