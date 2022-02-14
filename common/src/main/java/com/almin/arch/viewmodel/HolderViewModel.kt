package com.almin.arch.viewmodel

import android.os.Bundle
import com.almin.arch.viewmodel.Contract.LoadStatus
import com.almin.arch.viewmodel.Contract.PageEvent
import com.almin.arch.viewmodel.Contract.PageState

/**
 * Created by Almin on 2019-09-05.
 * 默认占位通用viewmodel
 */
class HolderViewModel : AbstractViewModel<PageState, PageEvent, Contract.PageEffect>(null) {

    override fun initialState(): PageState = State.Default

    override fun attach(arguments: Bundle?) {

    }


    sealed class State : PageState {
        object Default: State() {
            override val loadingState: LoadStatus = LoadStatus.Default
        }
    }


    override fun handleEvent(event: PageEvent) {
    }

}