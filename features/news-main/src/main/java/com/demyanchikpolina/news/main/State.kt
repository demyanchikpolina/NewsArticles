package com.demyanchikpolina.news.main

sealed class State {

    object None : State()

    class Loading(val articles: List<ArticleViewModel>?) : State()

    class Error(val error: String? = null) : State()

    class Success(val articles: List<ArticleViewModel>) : State()
}