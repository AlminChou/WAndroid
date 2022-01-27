/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.almin.wandroid.data.network.converter.scalar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter
import java.io.IOException

internal class CustomScalarRequestBodyConverter<T> private constructor() : Converter<T, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        return value.toString().toRequestBody(MEDIA_TYPE)
    }

    companion object {
        val INSTANCE = CustomScalarRequestBodyConverter<Any>()
        private val MEDIA_TYPE = "text/plain; charset=UTF-8".toMediaTypeOrNull()
    }
}
