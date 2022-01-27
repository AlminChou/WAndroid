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

import com.squareup.moshi.Moshi
import java.lang.reflect.Type

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * A [converter][Converter.Factory] for strings and both primitives and their boxed types
 * to `text/plain` bodies.
 */
class CustomScalarsConverterFactory private constructor(private val moshi: Moshi) : Converter.Factory() {

    override fun requestBodyConverter(type: Type?,
                                      parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        return if (type === String::class.java
                || type === Boolean::class.javaPrimitiveType
                || type === Boolean::class.java
                || type === Byte::class.javaPrimitiveType
                || type === Byte::class.java
                || type === Char::class.javaPrimitiveType
                || type === Char::class.java
                || type === Double::class.javaPrimitiveType
                || type === Double::class.java
                || type === Float::class.javaPrimitiveType
                || type === Float::class.java
                || type === Int::class.javaPrimitiveType
                || type === Int::class.java
                || type === Long::class.javaPrimitiveType
                || type === Long::class.java
                || type === Short::class.javaPrimitiveType
                || type === Short::class.java) {
            CustomScalarRequestBodyConverter.INSTANCE
        } else null
    }

    override fun responseBodyConverter(
            type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        CustomScalarResponseBodyConverters.moshi = moshi
        if (type === String::class.java) {
            return CustomScalarResponseBodyConverters.StringResponseBodyConverter.INSTANCE
        }
        if (type === Boolean::class.java || type === Boolean::class.javaPrimitiveType) {
            return CustomScalarResponseBodyConverters.BooleanResponseBodyConverter.INSTANCE
        }
        if (type === Byte::class.java || type === Byte::class.javaPrimitiveType) {
            return CustomScalarResponseBodyConverters.ByteResponseBodyConverter.INSTANCE
        }
        if (type === Char::class.java || type === Char::class.javaPrimitiveType) {
            return CustomScalarResponseBodyConverters.CharacterResponseBodyConverter.INSTANCE
        }
        if (type === Double::class.java || type === Double::class.javaPrimitiveType) {
            return CustomScalarResponseBodyConverters.DoubleResponseBodyConverter.INSTANCE
        }
        if (type === Float::class.java || type === Float::class.javaPrimitiveType) {
            return CustomScalarResponseBodyConverters.FloatResponseBodyConverter.INSTANCE
        }
        if (type === Int::class.java || type === Int::class.javaPrimitiveType) {
            return CustomScalarResponseBodyConverters.IntegerResponseBodyConverter.INSTANCE
        }
        if (type === Long::class.java || type === Long::class.javaPrimitiveType) {
            return CustomScalarResponseBodyConverters.LongResponseBodyConverter.INSTANCE
        }
        return if (type === Short::class.java || type === Short::class.javaPrimitiveType) {
            CustomScalarResponseBodyConverters.ShortResponseBodyConverter.INSTANCE
        } else null
    }

    companion object {
        fun create(moshi: Moshi): CustomScalarsConverterFactory {
            return CustomScalarsConverterFactory(moshi)
        }
    }
}
