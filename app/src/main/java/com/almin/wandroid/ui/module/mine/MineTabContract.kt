package com.almin.wandroid.ui.module.mine

import com.almin.arch.viewmodel.Contract

/**
 * Created by Almin on 2022/2/11.
 */
interface MineTabContract {
    sealed class PageState : Contract.PageState{
        object Idle : PageState()
    }

    sealed class PageEvent : Contract.PageEvent {
        object Logout : PageEvent()
        object ClickWanLink : PageEvent()
        object ClickSetting : PageEvent()
        object ClickIntegralRule : PageEvent()
    }

    sealed class PageEffect : Contract.PageEffect{
        object AlertLogoutDialog : PageEffect()
        object Navigation2WanPage: PageEffect()
        object Navigation2SettingPage: PageEffect()
        object Navigation2IntegralRulePage: PageEffect()
    }
}