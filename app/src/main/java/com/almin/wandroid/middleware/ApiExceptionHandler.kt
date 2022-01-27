package com.almin.wandroid.middleware

import android.widget.Toast
import com.almin.arch.middleware.ExceptionHandler
import com.almin.wandroid.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Created by Almin on 2022/1/4.
 */
class ApiExceptionHandler : ExceptionHandler {

    override fun handle(throwable: Throwable) {
        Toast.makeText(App.instance, throwable.localizedMessage, Toast.LENGTH_LONG).show()
    }
}