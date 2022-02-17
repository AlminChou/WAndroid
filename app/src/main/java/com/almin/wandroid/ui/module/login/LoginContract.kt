package com.almin.wandroid.ui.module.login

import com.almin.arch.viewmodel.Contract.*
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.UserInfo

interface LoginContract{
    data class State(val userInfo: UserInfo? = null, val loadStatus : LoadStatus = LoadStatus.Default) : PageState()

    sealed class Event: PageEvent{
        data class ClickLogin(val userName: String, val password: String) : Event()
    }
}