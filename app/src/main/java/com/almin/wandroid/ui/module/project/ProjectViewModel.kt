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
            is ProjectContract.PageEvent.LoadProjectListByTab -> {
                loadProjectList(categoryId, currentPage, !event.refresh)
            }
        }
    }

    private fun loadProjectList(cid: Int, page: Int, isLoadMore: Boolean) {
        if(!isLoadMore){
            currentPage = 0
        }
        api<PagerResponse<Article>> {
            call {
                val pager = if(cid == 0) projectRepository.getLatestProjectList(page) else projectRepository.getProjectListByCategoryId(cid, page)
                delay(1000) // 展示动画
                pager
            }
            prepare {
                setState { copy(loadStatus = if(isLoadMore) LoadStatus.LoadMore else LoadStatus.Refresh) }
            }
            success {
                currentPage = it.curPage
                setState { copy(projects = it.datas, loadStatus = if(isLoadMore) LoadStatus.LoadMoreFinish else LoadStatus.Finish) }
            }
            failed {
                setState { copy(loadStatus = if(isLoadMore) LoadStatus.LoadMoreFailed else LoadStatus.LoadFailed) }
            }
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