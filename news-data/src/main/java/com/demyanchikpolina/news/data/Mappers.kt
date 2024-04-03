package com.demyanchikpolina.news.data

import com.demyanchikpolina.database.models.ArticleDBO
import com.demyanchikpolina.database.models.SourceDBO
import com.demyanchikpolina.news.data.models.Article
import com.demyanchikpolina.news.data.models.Source
import com.demyanchikpolina.newsapi.models.ArticleDTO

internal fun ArticleDBO.toArticle(): Article =
    Article(
        articleId = articleId,
        source = Source(
            sourceId = source.sourceId,
            name = source.name
        ),
        author = author,
        title = title,
        description,
        url,
        urlToImage,
        publishedAt,
        content
    )

internal fun ArticleDTO.toArticle(): Article =
    TODO()
 /*   Article(
        articleId = ,
        source = Source(
            sourceId = source.id,
            name = source.name
        ),
        author = author,
        title = title,
        description,
        url,
        urlToImage,
        publishedAt,
        content
    )*/

internal fun ArticleDTO.toArticleDBO(): ArticleDBO =
    TODO()
//    ArticleDBO(
//        articleId = ?,
//        source = SourceDBO(
//            sourceId = source.id,
//            name = source.name
//        ),
//        author = author,
//        title = title,
//        description,
//        url,
//        urlToImage,
//        publishedAt,
//        content
//    )