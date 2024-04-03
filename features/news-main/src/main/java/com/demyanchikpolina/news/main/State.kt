package com.demyanchikpolina.news.main

sealed class State {

    object None : State()

    class Loading(val articles: List<ArticleViewModel>? = null) : State()

    class Error(
        val articles: List<ArticleViewModel>? = null,
        val error: String? = null
    ) : State()

    class Success(val articles: List<ArticleViewModel>) : State()
}