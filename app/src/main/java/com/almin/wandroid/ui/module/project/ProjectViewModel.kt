package com.almin.wandroid.ui.module.project

import android.os.Bundle
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.LoadStatus
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Project
import com.almin.wandroid.data.model.ProjectTabInfo
import com.almin.wandroid.data.repository.ProjectRepository
import kotlinx.coroutines.delay

class ProjectViewModel(private val projectRepository: ProjectRepository) : AbstractViewModel<ProjectContract.PageState,ProjectContract.PageEvent, Contract.PageEffect>(null) {

    private var currentPage = 0

    override fun initialState(): ProjectContract.PageState = ProjectContract.PageState()

    override fun attach(arguments: Bundle?) {
    }

    override fun handleEvent(event: ProjectContract.PageEvent) {
        when(event){
            is ProjectContract.PageEvent.LoadCategoryTab -> loadProjectCategory()
            is ProjectContract.PageEvent.LoadProjectListByTab -> {
                currentPage = 0
                loadProjectList(currentPage)
            }
            is ProjectContract.PageEvent.LoadNextProjectList -> {
                loadProjectList(currentPage)
            }
        }
    }

    private fun loadProjectList(page: Int) {
        val isLoadMore = page != 0
        api<List<Article>> {
            call {
                val pager = projectRepository.getLatestProjectList(page)
                currentPage = pager.curPage
                delay(1000) // 展示动画
                pager.datas
            }
            prepare {
                setState { copy(loadStatus = if(isLoadMore) LoadStatus.LoadMore else LoadStatus.Refresh) }
            }
            success {
                setState { copy(projects = it, loadStatus = if(isLoadMore) LoadStatus.LoadMoreFinish else LoadStatus.Finish) }
            }
            failed {
                setState { copy(loadStatus = if(isLoadMore) LoadStatus.LoadMoreFailed else LoadStatus.LoadFailed) }
            }
        }
    }

    private fun loadProjectCategory(){
        api<ProjectTabInfo> {
            call { projectRepository.getCategory() }
            success {
                setState { copy(tabInfo = it, loadStatus = LoadStatus.Finish) }
            }
            failed {
            }
        }
    }

}