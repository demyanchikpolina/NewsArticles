package com.demyanchikpolina.news.main

import com.demyanchikpolina.news.data.ArticlesRepository
import com.demyanchikpolina.news.data.RequestResult
import com.demyanchikpolina.news.data.map
import com.demyanchikpolina.news.data.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllArticlesUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository,
) {

    operator fun invoke(query: String): Flow<RequestResult<List<ArticleUI>>> {
        return articlesRepository.getAll(query)
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map { it.toArticleUI() }
                }
            }
    }
}

private fun Article.toArticleUI(): ArticleUI =
    ArticleUI(
        id = cacheId,
        title = title ?: "NO TITLE",
        description = description,
        imageUrl = urlToImage,
        url = url,
    )