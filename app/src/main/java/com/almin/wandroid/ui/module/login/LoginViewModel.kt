package com.almin.wandroid.ui.module.login

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.repository.Status
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.data.repository.UserRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2022/1/4.
 */
class LoginViewModel(middleWareProvider: MiddleWareProvider, private val userRepository: UserRepository)
    : AbstractViewModel<LoginContract.State, LoginContract.Event, Contract.PageEffect>(middleWareProvider) {

    override fun initialState(): LoginContract.State = LoginContract.State()

    override fun attach(arguments: Bundle?) {
    }

    private fun login(account: String, password: String){
//        viewModelScope.launch {
//            flow {
//                emit(userRepository.login(account, password))
//            }.onStart {
//                setState { copy(loadStatus = LoadStatus.Loading)}
//            }.apiCatch {
//                setState { copy(loadStatus = LoadStatus.LoadFailed) }
//            }.collect{
//                setState { copy(userInfo = it, loadStatus = LoadStatus.Finish)}
//            }
//        }

        api(userRepository.login(account, password)){
            prepare {
                setState { copy(loadStatus = LoadStatus.Loading)}
            }
            cache {
            }
            success {
                setState { copy(userInfo = it, loadStatus = LoadStatus.Finish)}
            }
            failed {
                setState { copy(loadStatus = LoadStatus.LoadFailed) }
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