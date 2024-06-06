package com.demyanchikpolina.news.main

internal sealed class State(val articles: List<ArticleUI>?) {

    data object None : State(articles = null)

    class Loading(articles: List<ArticleUI>? = null) : State(articles)

    class Error(
        articles: List<ArticleUI>? = null,
        val error: String? = null
    ) : State(articles)

    class Success(articles: List<ArticleUI>) : State(articles)
}
