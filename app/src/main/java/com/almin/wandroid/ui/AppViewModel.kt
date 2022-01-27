package com.almin.wandroid.ui

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.data.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Almin on 2022/1/21.
 * Gobal  viewModel
 */
class AppViewModel(middleWareProvider: MiddleWareProvider, private val userRepository: UserRepository) : AbstractViewModel<Contract.PageState, Contract.PageEvent>(middleWareProvider) {

    private var userInfo: UserInfo? = null

    override fun initialState(): Contract.PageState = Contract.PageState.Default

    override fun attach(arguments: Bundle?) {
        viewModelScope.launch {
            userRepository.loadUserInfo().collect{
                if(!it.isNullOrEmpty()){
                    userInfo = it.first()
                }
            }
        }
    }

    override fun handleEvent(event: Contract.PageEvent) {
    }

    fun updateUser(userInfo: UserInfo) {
        this.userInfo = userInfo
        viewModelScope.launch {
            userRepository.updateUserCache(userInfo = userInfo)
        }
    }
}