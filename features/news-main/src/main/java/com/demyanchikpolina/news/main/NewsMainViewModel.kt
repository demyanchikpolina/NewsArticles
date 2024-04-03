package com.demyanchikpolina.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demyanchikpolina.news.data.RequestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class NewsMainViewModel(
    private val getAllArticlesUseCase: GetAllArticlesUseCase
) : ViewModel() {

    val state: StateFlow<State> =
        getAllArticlesUseCase()
            .map { it.toState() }
            .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
}

private fun RequestResult<List<ArticleViewModel>>.toState() : State =
    when(this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(checkNotNull(data))
    }
