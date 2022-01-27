package com.almin.arch.permission

/**
 * Created by Almin on 2020/7/20.
 */
class SinglePermissionCallbackDsl {
     var granted: (permission: String) -> Unit = {}
     var denied: (permission: String) -> Unit = {}
     var explained: (permission: String) -> Unit = {}
}

class MultiPermissionCallbackDsl {
     var allGranted: () -> Unit = {}
     var denied: (List<String>) -> Unit = {}
     var explained: (List<String>) -> Unit = {}
}
