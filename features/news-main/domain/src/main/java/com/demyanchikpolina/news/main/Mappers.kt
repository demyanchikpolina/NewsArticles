package com.demyanchikpolina.news.main

import com.demyanchikpolina.news.data.RequestResult
import com.demyanchikpolina.news.data.models.Article
import kotlinx.collections.immutable.toImmutableList

internal fun RequestResult<List<ArticleUI>>.toState(): State =
    when (this) {
        is RequestResult.Error -> State.Error(data?.toImmutableList())
        is RequestResult.InProgress -> State.Loading(data?.toImmutableList())
        is RequestResult.Success -> State.Success(data.toImmutableList())
    }

internal fun Article.toArticleUI(): ArticleUI =
    ArticleUI(
        id = cacheId,
        title = title ?: "NO TITLE",
        description = description,
        imageUrl = urlToImage,
        url = url,
    )
