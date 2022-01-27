package com.almin.wandroid.data.network.converter.scalar

import android.util.Log
import com.almin.arch.network.exception.BusinessServiceException
import com.almin.wandroid.data.model.ApiResponse
import com.squareup.moshi.Moshi
import java.io.IOException
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import org.json.JSONTokener


internal object CustomScalarResponseBodyConverters {
    var moshi: Moshi? = null

    internal class StringResponseBodyConverter : Converter<ResponseBody, String> {
//        private val gson = Gson()

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {

            var isJsonFile = false  //没有body result之类字段默认是访问json文件

            var response = value.string()
//            val type = object : TypeToken<ApiResponse>() {}.type

            val resObj = JSONTokener(response).nextValue()
            if(resObj is JSONArray){
                return response
            }


            val apiResponse: ApiResponse? = moshi!!.adapter(ApiResponse::class.java).fromJson(response)
            apiResponse?.run {
                Log.d("ResponseBodyConverter", "api call status  " + apiResponse.isSuccess)

                try {
                    val responseJson = JSONObject(response)
                    if(responseJson.has(ApiResponse.API_RESULT_BODY_KEY) && !responseJson.isNull(ApiResponse.API_RESULT_BODY_KEY)){
                        response = responseJson.opt(ApiResponse.API_RESULT_BODY_KEY).toString()
                    }else{
                        isJsonFile = true
                    }
                }catch (e: JSONException){
                    Log.d("ResponseBodyConverter", "api call status model json format error.")
                }

                if(!isJsonFile){
                    if (!isSuccess) {
                        value.close()
                        // 向上层转译异常
                        throw BusinessServiceException(errorCode, errorMsg, response)
                    }
                }
            }

            return response
        }

        companion object {
            val INSTANCE = StringResponseBodyConverter()
        }
    }

    internal class BooleanResponseBodyConverter : Converter<ResponseBody, Boolean> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Boolean {
            return java.lang.Boolean.valueOf(value.string())
        }

        companion object {
            val INSTANCE = BooleanResponseBodyConverter()
        }
    }

    internal class ByteResponseBodyConverter : Converter<ResponseBody, Byte> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Byte {
            return java.lang.Byte.valueOf(value.string())
        }

        companion object {
            val INSTANCE = ByteResponseBodyConverter()
        }
    }

    internal class CharacterResponseBodyConverter : Converter<ResponseBody, Char> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Char {
            val body = value.string()
            if (body.length != 1) {
                throw IOException(
                    "Expected body of length 1 for Character conversion but was " + body.length)
            }
            return body[0]
        }

        companion object {
            val INSTANCE = CharacterResponseBodyConverter()
        }
    }

    internal class DoubleResponseBodyConverter : Converter<ResponseBody, Double> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Double {
            return java.lang.Double.valueOf(value.string())
        }

        companion object {
            val INSTANCE = DoubleResponseBodyConverter()
        }
    }

    internal class FloatResponseBodyConverter : Converter<ResponseBody, Float> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Float {
            return java.lang.Float.valueOf(value.string())
        }

        companion object {
            val INSTANCE = FloatResponseBodyConverter()
        }
    }

    internal class IntegerResponseBodyConverter : Converter<ResponseBody, Int> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Int {
            return Integer.valueOf(value.string())
        }

        companion object {
            val INSTANCE = IntegerResponseBodyConverter()
        }
    }

    internal class LongResponseBodyConverter : Converter<ResponseBody, Long> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Long {
            return java.lang.Long.valueOf(value.string())
        }

        companion object {
            val INSTANCE = LongResponseBodyConverter()
        }
    }

    internal class ShortResponseBodyConverter : Converter<ResponseBody, Short> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): Short? {
            return java.lang.Short.valueOf(value.string())
        }

        companion object {
            val INSTANCE = ShortResponseBodyConverter()
        }
    }
}
