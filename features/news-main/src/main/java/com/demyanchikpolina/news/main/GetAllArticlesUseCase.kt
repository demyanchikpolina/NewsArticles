package com.demyanchikpolina.news.main

import com.demyanchikpolina.news.data.ArticlesRepository
import com.demyanchikpolina.news.data.RequestResult
import com.demyanchikpolina.news.data.map
import com.demyanchikpolina.news.data.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllArticlesUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository,
) {

    operator fun invoke(): Flow<RequestResult<List<ArticleViewModel>>> {
        return articlesRepository.getAll()
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map { it.toViewModel() }
                }
            }
    }
}

private fun Article.toViewModel(): ArticleViewModel =
    ArticleViewModel("Not implemented")