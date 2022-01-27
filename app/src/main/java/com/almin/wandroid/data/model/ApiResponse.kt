package com.almin.wandroid.data.model

/**
 * Created by Almin on 2022/1/4.
 */
data class ApiResponse(
//    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
) {
    val isSuccess = errorCode == 0

    companion object{
        const val API_RESULT_BODY_KEY = "data"
    }
}

