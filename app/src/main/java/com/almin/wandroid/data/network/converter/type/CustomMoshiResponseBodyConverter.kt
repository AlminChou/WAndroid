package com.almin.wandroid.data.network.converter.type

import android.util.Log
import com.almin.arch.network.exception.BusinessServiceException
import com.almin.wandroid.data.model.ApiResponse
import com.almin.wandroid.data.network.converter.scalar.CustomScalarResponseBodyConverters
import com.squareup.moshi.*
import okhttp3.ResponseBody
import okhttp3.internal.readBomAsCharset
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8

internal class CustomMoshiResponseBodyConverter<T>(private val adapter: JsonAdapter<T>, private val moshi: Moshi) :
    Converter<ResponseBody, T?> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        val source = value.source()

        return try {
            // Moshi has no document-level API so the responsibility of BOM skipping falls to whatever
            // is delegating to it. Since it's a UTF-8-only library as well we only honor the UTF-8 BOM.
            if (source.rangeEquals(0, UTF8_BOM)) {
                source.skip(UTF8_BOM.size.toLong())
            }

            var bodyResponse = source.readString(charset = source.readBomAsCharset(UTF_8))
//            val reader = JsonReader.of(source)

            var isJsonFile = false

            val apiResponse: ApiResponse? = moshi.adapter(ApiResponse::class.java).fromJson(bodyResponse)
            if(apiResponse != null){
                Log.d("ResponseBodyConverter", "api call status  " + apiResponse.isSuccess)

                try {
                    val responseJson = JSONObject(bodyResponse)
                    if(responseJson.has(ApiResponse.API_RESULT_BODY_KEY)){  // responseJson.isNull(ApiResponse.API_RESULT_BODY_KEY)
                        bodyResponse = responseJson.opt(ApiResponse.API_RESULT_BODY_KEY).toString()
                    }else{
                        isJsonFile = true
                    }
                }catch (e: JSONException){
                    Log.d("ResponseBodyConverter", "api call status model json format error.")
                }

                if(!isJsonFile){
                    if (!apiResponse.isSuccess) {
                        value.close()
                        // 向上层转译异常
                        throw BusinessServiceException(apiResponse.errorCode, apiResponse.errorMsg, bodyResponse)
                    }
                }

                val result = adapter.fromJson(bodyResponse)
//                if (reader.peek() != JsonReader.Token.END_DOCUMENT) {
//                    throw JsonDataException("JSON document was not fully consumed.")
//                }
                result
            }else {
                val reader = JsonReader.of(source)
                val result = adapter.fromJson(reader)
                if (reader.peek() != JsonReader.Token.END_DOCUMENT) {
                    throw JsonDataException("JSON document was not fully consumed.")
                }
                result
            }
        } finally {
            value.close()
        }
    }

    companion object {
        private val UTF8_BOM: ByteString = "EFBBBF".decodeHex()
    }
}