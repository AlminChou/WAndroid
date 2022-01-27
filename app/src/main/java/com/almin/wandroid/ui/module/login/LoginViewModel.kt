package com.almin.wandroid.ui.module.login

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.repository.Status
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.data.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2022/1/4.
 */
class LoginViewModel(middleWareProvider: MiddleWareProvider, private val userRepository: UserRepository) : AbstractViewModel<LoginContract.State, LoginContract.Event>(middleWareProvider) {

    override fun initialState(): LoginContract.State = LoginContract.State.Default

    override fun attach(arguments: Bundle?) {
    }

    private fun login(account: String, password: String){
//        viewModelScope.launch {
//            flow {
//                emit(userRepository.login(account, password))
//            }.onStart {
//                setState { LoginContract.State.Logining }
//            }.apiCatch {
//                setState { LoginContract.State.LoginFailed }
//            }.collect{
//                setState { LoginContract.State.LoginSuccess(it) }
//            }
//        }

        api(userRepository.login(account, password)){
            prepare {
                setState { LoginContract.State.Logining }
            }
            cache {
            }
            success {
                setState { LoginContract.State.LoginSuccess(it) }
            }
            failed {
                setState { LoginContract.State.LoginFailed }
            }
        }
    }

    override fun handleEvent(event: LoginContract.Event) {
        when(event){
            is LoginContract.Event.ClickLogin -> {
                login(event.userName, event.password)
            }
        }
    }


}