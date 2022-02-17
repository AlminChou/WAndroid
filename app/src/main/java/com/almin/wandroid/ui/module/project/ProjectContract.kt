package com.almin.wandroid.ui.module.project

import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Project
import com.almin.wandroid.data.model.ProjectCategory
import com.almin.wandroid.data.model.ProjectTabInfo

/**
 * Created by Almin on 2022/2/16.
 */
interface ProjectContract {

    data class PageState(val tabInfo: ProjectTabInfo? = null, val projects: List<Article>? = null, val loadStatus: LoadStatus = LoadStatus.Default) : Contract.PageState()

    sealed class PageEvent : Contract.PageEvent{
        object LoadCategoryTab : PageEvent()
        object LoadProjectListByTab : PageEvent()
    }

}