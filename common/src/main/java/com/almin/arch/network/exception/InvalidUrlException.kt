package com.almin.arch.network.exception

import android.text.TextUtils

class InvalidUrlException(url: String) :
        RuntimeException("You've configured an invalid url : " +
                if (TextUtils.isEmpty(url)) "EMPTY_OR_NULL_URL" else url)