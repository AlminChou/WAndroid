package com.almin.wandroid.ui.module.register

import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.UserInfo

/**
 * Created by Almin on 2022/1/6.
 */
interface RegisterContract {
    data class State(val userInfo: UserInfo? = null, val loadStatus: LoadStatus = LoadStatus.Default) : Contract.PageState()

    sealed class Event: Contract.PageEvent {
        data class ClickSignIn(val userName: String, val password: String, val repeat: String) : Event()
    }
}