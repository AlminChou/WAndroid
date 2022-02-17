package com.almin.wandroid.ui.module.register

import android.os.Bundle
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.data.repository.UserRepository

/**
 * Created by Almin on 2022/1/4.
 */
class RegisterViewModel(middleWareProvider: MiddleWareProvider, private val userRepository: UserRepository)
    : AbstractViewModel<RegisterContract.State, RegisterContract.Event, Contract.PageEffect>(middleWareProvider) {

    override fun initialState(): RegisterContract.State = RegisterContract.State()

    override fun attach(arguments: Bundle?) {
    }

    private fun register(account: String, password: String, repeat: String){
        api(userRepository.register(account, password, repeat)){
            prepare { setState { currentState.copy(loadStatus = LoadStatus.Loading) }}
            success {
                setState { currentState.copy(userInfo = it, loadStatus = LoadStatus.Finish)}
            }
            failed {
                setState { currentState.copy(loadStatus = LoadStatus.LoadFailed)}
            }
        }


        // 简约请求 dsl
//        api<UserInfo> {
//            call { userRepository.register(account, password, repeat) }
//            prepare { setState { currentState.copy(loadStatus = LoadStatus.Loading) }}
//            success {
//                setState { currentState.copy(userInfo = it, loadStatus = LoadStatus.Finish)}
//            }
//            failed {
//                setState { currentState.copy(loadStatus = LoadStatus.LoadFailed)}
//            }
//        }

        // flow 形式
//        viewModelScope.launch {
//            flow<UserInfo> {
//
//            }.onStart {
//
//            }.apiCatch {

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