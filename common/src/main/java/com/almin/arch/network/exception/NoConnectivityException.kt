package com.almin.arch.network.exception

import java.io.IOException

class NoConnectivityException : IOException() {
        override val message: String
            get() = "No connectivity exception"
    }