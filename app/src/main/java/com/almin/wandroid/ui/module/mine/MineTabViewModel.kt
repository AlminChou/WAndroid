package com.almin.wandroid.ui.module.mine

import android.os.Bundle
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract

/**
 * Created by Almin on 2022/2/11.
 */
class MineTabViewModel(middleWareProvider: MiddleWareProvider) : AbstractViewModel<MineTabContract.PageState, MineTabContract.PageEvent, MineTabContract.PageEffect>(middleWareProvider) {

    override fun initialState(): MineTabContract.PageState  = MineTabContract.PageState.Idle

    override fun attach(arguments: Bundle?) {
    }

    override fun handleEvent(event: MineTabContract.PageEvent) {
        when(event){
            is MineTabContract.PageEvent.Logout -> setEffect { MineTabContract.PageEffect.AlertLogoutDialog }
            is MineTabContract.PageEvent.ClickWanLink -> setEffect { MineTabContract.PageEffect.Navigation2WanPage  }
            is MineTabContract.PageEvent.ClickIntegralRule -> setEffect { MineTabContract.PageEffect.Navigation2IntegralRulePage  }
            is MineTabContract.PageEvent.ClickSetting -> setEffect { MineTabContract.PageEffect.Navigation2SettingPage  }
        }
    }
}