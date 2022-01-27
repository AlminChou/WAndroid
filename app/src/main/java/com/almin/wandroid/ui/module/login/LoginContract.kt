package com.almin.wandroid.ui.module.login

import com.almin.arch.viewmodel.Contract.*
import com.almin.wandroid.data.model.UserInfo

interface LoginContract{
    sealed class State(loadStatus: LoadStatus) : PageState {
        override val loadingState: LoadStatus = loadStatus
        object Default: State(LoadStatus.Default)
        object Logining: State(LoadStatus.Loading)
        object LoginFailed: State(LoadStatus.LoadFailed)
        data class LoginSuccess(val userInfo: UserInfo): State(LoadStatus.Finish)
    }

    sealed class Event: PageEvent{
        data class ClickLogin(val userName: String, val password: String) : Event()
    }
}