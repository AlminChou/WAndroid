package com.almin.wandroid.ui

import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.UserInfo


/**
 * Created by Almin on 2022/2/14.
 * gobal state & event
 */
interface AppContract {

    sealed class Event(val userInfo: UserInfo? = null) : Contract.PageEvent{
        object OpenApp : Event()
        object Login : Event()
        object Logout: Event()
        object UpdateUser: Event()
    }

    sealed class State : Contract.PageState{
        object Idle : State()
        data class LoginSuccess(val userInfo: UserInfo) : State()
        object LogoutSuccess : State()
    }

    sealed class Effect : Contract.PageEffect{
        object Navigation2Login: Effect()
    }
}