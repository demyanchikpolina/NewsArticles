package com.demyanchikpolina.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demyanchikpolina.news.data.ArticlesRepository
import com.demyanchikpolina.news.data.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor(
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
    //private val articlesRepository: ArticlesRepository,
) : ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke()
            .map { it.toState() }
            .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun forceUpdate() {
        //articlesRepository.fetchLatest()
    }
}

private fun RequestResult<List<ArticleViewModel>>.toState() : State =
    when(this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
    }
