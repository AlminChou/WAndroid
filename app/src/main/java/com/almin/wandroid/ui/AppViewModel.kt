package com.almin.wandroid.ui

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2022/1/21.
 * Gobal  viewModel
 */
class AppViewModel(middleWareProvider: MiddleWareProvider, private val userRepository: UserRepository) : AbstractViewModel<AppContract.State, AppContract.Event, AppContract.Effect>(middleWareProvider) {

    // 需要一个 1对多 被订阅的flow
    private val _commonState : MutableSharedFlow<AppContract.State> = MutableSharedFlow(replay = 1, extraBufferCapacity = Int.MAX_VALUE)
    val commonState = _commonState.asSharedFlow()

    private var userInfo: UserInfo? = null

    override fun initialState(): AppContract.State = AppContract.State.Idle

    override fun attach(arguments: Bundle?) {
        viewModelScope.launch {
            userRepository.loadUserInfo().collect{
                if(!it.isNullOrEmpty()){
                    userInfo = it.first()
                    setCommonState(AppContract.State.LoginSuccess(userInfo!!))
                }
            }
        }
    }

    override fun handleEvent(event: AppContract.Event) {
        when(event){
            is AppContract.Event.Login -> {
                setEffect { AppContract.Effect.Navigation2Login }
            }
            is AppContract.Event.UpdateUser -> {
                event.userInfo?.let { updateUser(it) }
            }
            is AppContract.Event.Logout -> {
                logout()
            }
        }
    }

    private fun logout() {
        userInfo?.run {
            viewModelScope.launch {
                userRepository.cleanUserInfo(userInfo = this@run)
                userInfo = null
            }
        }
        setCommonState(AppContract.State.LogoutSuccess)
    }

    private fun updateUser(userInfo: UserInfo) {
        this.userInfo = userInfo
        viewModelScope.launch {
            userRepository.updateUserCache(userInfo = userInfo)
        }
    }

    fun isLogined(): Boolean{
        return userInfo != null
    }

    private fun setCommonState(state: AppContract.State){
        viewModelScope.launch { _commonState.emit(state) }
    }
}