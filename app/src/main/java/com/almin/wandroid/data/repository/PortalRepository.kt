package com.almin.wandroid.data.repository

import com.almin.arch.repository.Repository
import com.almin.wandroid.data.model.TAG_TYPE_ARTICLE
import com.almin.wandroid.data.model.TAG_TYPE_CATEGORY
import com.almin.wandroid.data.model.TagNode
import com.almin.wandroid.data.model.TagTree
import com.almin.wandroid.data.network.api.PortalApiService

/**
 * Created by Almin on 2022/3/29.
 */
class PortalRepository(private val articleRepository: ArticleRepository,
                       private val portalApiService: PortalApiService) : Repository() {


    suspend fun loadSystemTree() = portalApiService.getSystemTree().map { systemNode ->
        TagTree(
            groupName = systemNode.name,
            list = systemNode.children.map {
                TagNode(
                    projectCategory = it,
                    id = it.id,
                    article = null,
                    name = it.name,
                    type = TAG_TYPE_CATEGORY
                )
            }
        )
    }

    suspend fun loadNavigationTree() = portalApiService.getNavigationTree().map {
        TagTree(
            groupName = it.name,
            list = it.articles.map { article ->
                TagNode(
                    projectCategory = null,
                    id = article.id,
                    article = article,
                    name = article.title,
                    type = TAG_TYPE_ARTICLE
                )
            }
        )
    }

}