package com.almin.arch.permission

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment

/**
 * Created by Almin on 2020/7/20.
 */
fun ComponentActivity.permission(permission: String, dsl: SinglePermissionCallbackDsl.()->Unit){
    val callbackDsl = SinglePermissionCallbackDsl().apply(dsl)
    requestPermission(permission, callbackDsl.granted, callbackDsl.denied, callbackDsl.explained)
}

fun ComponentActivity.permissions(permissions: Array<String>, dsl: MultiPermissionCallbackDsl.()->Unit){
    val callbackDsl = MultiPermissionCallbackDsl().apply(dsl)
    requestMultiplePermissions(permissions, allGranted = callbackDsl.allGranted, denied = callbackDsl.denied, explained = callbackDsl.explained)
}

fun Fragment.permission(permission: String, dsl: SinglePermissionCallbackDsl.()->Unit){
    val callbackDsl = SinglePermissionCallbackDsl().apply(dsl)
    requestPermission(permission, callbackDsl.granted, callbackDsl.denied, callbackDsl.explained)
}

fun Fragment.permissions(permissions: Array<String>, dsl: MultiPermissionCallbackDsl.()->Unit){
    val callbackDsl = MultiPermissionCallbackDsl().apply(dsl)
    requestMultiplePermissions(permissions, allGranted = callbackDsl.allGranted, denied = callbackDsl.denied, explained = callbackDsl.explained)
}