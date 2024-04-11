package com.demyanchikpolina.news.main

internal sealed class State {

    data object None : State()

    class Loading(val articles: List<ArticleUI>? = null) : State()

    class Error(
        val articles: List<ArticleUI>? = null,
        val error: String? = null
    ) : State()

    class Success(val articles: List<ArticleUI>) : State()
}