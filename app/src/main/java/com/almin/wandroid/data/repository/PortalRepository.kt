package com.almin.wandroid.data.repository

import com.almin.arch.repository.Repository
import com.almin.wandroid.data.model.TagTree
import com.almin.wandroid.data.network.api.PortalApiService

/**
 * Created by Almin on 2022/3/29.
 */
class PortalRepository(private val articleRepository: ArticleRepository,
                       private val portalApiService: PortalApiService) : Repository() {


    suspend fun loadSystemTree() = portalApiService.getSystemTree()

    suspend fun loadNavigationTree() = portalApiService.getNavigationTree()

}