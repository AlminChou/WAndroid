package com.almin.wandroid.ui.module.register

import com.almin.arch.viewmodel.Contract

/**
 * Created by Almin on 2022/1/6.
 */
interface RegisterContract {
    sealed class State(loadStatus: Contract.LoadStatus) : Contract.PageState {
        override val loadingState: Contract.LoadStatus = loadStatus
        object Default: State(Contract.LoadStatus.Default)
        object SignIning: State(Contract.LoadStatus.Loading)
        object SignInSuccess: State(Contract.LoadStatus.Finish)
        object SignInFailed: State(Contract.LoadStatus.LoadFailed)
    }

    sealed class Event: Contract.PageEvent {
        data class ClickSignIn(val userName: String, val password: String, val repeat: String) : Event()
    }
}