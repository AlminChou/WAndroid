package com.almin.wandroid.ui.module.project

import android.os.Bundle
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.const.Key
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.PagerResponse
import com.almin.wandroid.data.model.Project
import com.almin.wandroid.data.model.ProjectTabInfo
import com.almin.wandroid.data.repository.ProjectRepository
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.delay

class ProjectViewModel(private val projectRepository: ProjectRepository) : AbstractViewModel<ProjectContract.PageState,ProjectContract.PageEvent, Contract.PageEffect>(null) {

    private var currentPage = 0
    private var categoryId = 0

    override fun initialState(): ProjectContract.PageState = ProjectContract.PageState()

    override fun attach(arguments: Bundle?) {
        arguments?.run {
            categoryId = getInt(Key.BUNDLE_KEY_PROJECT_CATEGORY_ID)
        }
    }

    override fun handleEvent(event: ProjectContract.PageEvent) {
        when(event){
            is ProjectContract.PageEvent.LoadCategoryTab -> loadProjectCategory()
            else -> { }
        }
    }

    private fun loadProjectCategory(){
        api<ProjectTabInfo> {
            call { projectRepository.getCategory() }
            prepare {
                setState { copy(loadStatus = LoadStatus.Loading) }
            }
            success {
                setState { copy(tabInfo = it, loadStatus = LoadStatus.Finish) }
            }
            failed {
                setState { copy(loadStatus = LoadStatus.LoadFailed) }
            }
        }
    }

}