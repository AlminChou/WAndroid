package com.almin.arch.scheduler

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Almin on 2019-07-07.
 */
class AppThreadSchedulers(private val diskIO: Executor, private val networkIO: Executor, private val mainThread: Executor) : SchedulersProvider {

    override fun diskIO(): Executor {
        return diskIO
    }

    override fun networkIO(): Executor {
        return networkIO
    }

    override fun mainThread(): Executor {
        return mainThread
    }

    override fun postToDiskIO(command: Runnable) {
        diskIO.execute(command)
    }

    override fun postToNetworkIO(command: Runnable) {
        networkIO.execute(command)
    }

    override fun postToUI(command: Runnable) {
        mainThread.execute(command)
    }

    companion object {
        private const val THREAD_COUNT = 3

        fun create(): SchedulersProvider {
            return AppThreadSchedulers(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT), MainThreadExecutor())
        }
    }
}