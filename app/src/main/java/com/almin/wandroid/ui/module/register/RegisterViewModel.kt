package com.almin.wandroid.ui.module.register

import android.os.Bundle
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.data.repository.UserRepository

/**
 * Created by Almin on 2022/1/4.
 */
class RegisterViewModel(middleWareProvider: MiddleWareProvider, private val userRepository: UserRepository)
    : AbstractViewModel<RegisterContract.State, RegisterContract.Event>(middleWareProvider) {

    override fun initialState(): RegisterContract.State = RegisterContract.State.Default

    override fun attach(arguments: Bundle?) {
    }

    private fun register(account: String, password: String, repeat: String){
        // 简约请求 dsl
        api<UserInfo> {
            call { userRepository.register(account, password, repeat) }
            prepare { setState { RegisterContract.State.SignIning} }
            success {
                setState { RegisterContract.State.SignInSuccess}
            }
            failed {
                setState { RegisterContract.State.SignInFailed }
            }
        }

        // flow 形式
//        viewModelScope.launch {
//            flow<UserInfo> {
//
//            }.onStart {
//
//            }.apiCatch {
//                setState { RegisterContract.State.SignInFailed }
//            }.collect()
//        }
    }

    override fun handleEvent(event: RegisterContract.Event) {
        when(event){
            is RegisterContract.Event.ClickSignIn -> {
                register(event.userName, event.password, event.repeat)
            }
        }
    }

}