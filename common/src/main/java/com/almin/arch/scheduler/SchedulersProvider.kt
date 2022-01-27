package com.almin.arch.scheduler

import java.util.concurrent.Executor


/**
 * Created by Almin on 2019-07-07.
 */
interface SchedulersProvider {
    abstract fun diskIO(): Executor
    abstract fun networkIO(): Executor
    abstract fun mainThread(): Executor

    abstract fun postToDiskIO(command: Runnable)
    abstract fun postToNetworkIO(command: Runnable)
    abstract fun postToUI(command: Runnable)
}