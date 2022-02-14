package com.almin.wandroid

import android.app.Application
import com.almin.wandroid.di.appModule
import com.almin.wandroid.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Almin on 2021/12/31.
 */

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            fragmentFactory()
            modules(appModule, networkModule)
        }

        xcrash.XCrash.init(this)
    }
}