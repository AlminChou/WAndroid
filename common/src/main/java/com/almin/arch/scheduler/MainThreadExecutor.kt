package com.almin.arch.scheduler

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * Created by Almin on 2019-07-07.
 */
class MainThreadExecutor : Executor {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        mainThreadHandler.post(command)
    }

}