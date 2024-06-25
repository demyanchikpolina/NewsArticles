package com.demyanchikpolina.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.demyanchikpolina.news.NewsSearchTheme

@Composable
fun NewsMainScreen(modifier: Modifier = Modifier) {
    NewsMainScreen(viewModel = viewModel(), modifier = modifier)
}

@Composable
internal fun NewsMainScreen(
    viewModel: NewsMainViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    if (state != State.None) {
        NewsMainContent(state, modifier)
    }
}

@Composable
private fun NewsMainContent(
    state: State,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        when (state) {
            is State.None -> Unit
            is State.Error -> ErrorMessage(state)
            is State.Loading -> ProgressIndicator(state)
            is State.Success -> ArticleList(state)
        }
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading) {
    Column {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        state.articles?.let { ArticleListContent(articles = it) }
    }
}

@Composable
private fun ErrorMessage(state: State.Error) {
    Column {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(NewsSearchTheme.colorScheme.error)
                    .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Error occurred during update",
                color = NewsSearchTheme.colorScheme.onError,
            )
        }
        state.articles?.let { ArticleListContent(articles = it) }
    }
}

@Composable
private fun ArticleList(state: State.Success) {
    ArticleListContent(articles = state.articles)
}
