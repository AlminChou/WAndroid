package com.almin.wandroid.data.repository

import com.almin.arch.repository.Repository
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.PagerResponse
import com.almin.wandroid.data.model.ProjectTabInfo
import com.almin.wandroid.data.network.api.ProjectApiService

/**
 * Created by Almin on 2022/2/16.
 */
class ProjectRepository(private val projectApiService: ProjectApiService) : Repository() {

    suspend fun getCategory() : ProjectTabInfo{
        val category = projectApiService.getProjectCategory()
        val titleList = category.map {
            it.name
        }
        return ProjectTabInfo(titleList, category)
    }

    suspend fun getProjectList(type: Int, pageNo: Int) = projectApiService.getProjecListByType(type, pageNo)

    suspend fun getLatestProjectList(pageNo: Int) : PagerResponse<Article> {
        val pager = projectApiService.getLatestProjectList(pageNo)
        pager.datas.forEach {
            it.articleType = Article.ARTICLE_TYPE_PROJECT
        }
        return pager
    }

}