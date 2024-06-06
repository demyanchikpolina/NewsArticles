package com.demyanchikpolina.news.data

import com.demyanchikpolina.database.models.ArticleDBO
import com.demyanchikpolina.database.models.SourceDBO
import com.demyanchikpolina.news.data.models.Article
import com.demyanchikpolina.news.data.models.Source
import com.demyanchikpolina.newsapi.models.ArticleDTO
import com.demyanchikpolina.newsapi.models.SourceDTO
import java.lang.Math.random

internal fun ArticleDBO.toArticle(): Article =
    Article(
        cacheId = articleId,
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )

internal fun SourceDBO.toSource(): Source =
    Source(sourceId = sourceId, name = name)

internal fun ArticleDTO.toArticle(): Article =
    Article(
        cacheId = random().toLong(),
        source = source?.toSource(),
        author = author,
        title = title,
        description,
        url,
        urlToImage,
        publishedAt,
        content
    )

internal fun SourceDTO.toSource(): Source? {
    if (id == null && name == null) {
        return null
    }
    return Source(sourceId = id, name = name)
}

internal fun ArticleDTO.toArticleDBO(): ArticleDBO =
    ArticleDBO(
        source = source?.toSourceDBO(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )

internal fun SourceDTO.toSourceDBO(): SourceDBO? {
    if (id == null && name == null) {
        return null
    }
    return SourceDBO(sourceId = id, name = name)
}
