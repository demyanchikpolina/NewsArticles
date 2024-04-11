package com.demyanchikpolina.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NewsMainScreen() {
    NewsMainScreen(viewModel = viewModel())
}
@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()
    when(val currentState = state) {
        is State.Success -> Articles(currentState.articles)
        is State.Error -> ArticlesWithError(currentState.articles)
        is State.Loading -> ArticlesUpdating(currentState.articles)
        State.None -> NewsEmpty()
    }
}

@Preview
@Composable
private fun ArticlesWithError(
    @PreviewParameter(ArticlesPreviewProvider::class) articles: List<ArticleUI>?
) {
    Column {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error occurred during update",
                color = MaterialTheme.colorScheme.onError
            )
        }
        articles?.let { Articles(articles = it) }
    }
}

@Preview
@Composable
private fun ArticlesUpdating(
    @PreviewParameter(ArticlesPreviewProvider::class) articles: List<ArticleUI>?
) {
    Column {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        articles?.let { Articles(articles = it) }
    }
}

@Composable
private fun NewsEmpty() {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "No news.")
    }
}

@Preview
@Composable
private fun Articles(
    @PreviewParameter(ArticlesPreviewProvider::class) articles: List<ArticleUI>
) {
    LazyColumn {
        items(articles) { article ->
            key(article.id) {
                ArticleItem(article)
            }
        }
    }
}

@Preview
@Composable
private fun ArticleItem(@PreviewParameter(ArticlePreviewProvider::class) article: ArticleUI) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = article.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3
        )
    }
}

private class ArticlePreviewProvider : PreviewParameterProvider<ArticleUI> {
    override val values = sequenceOf(
        ArticleUI(
            id = 1,
            title = "1 Use Hilt with other Jetpack libraries",
            description = "1 Hilt includes extensions for providing classes from other Jetpack libraries supports the following Jetpack components.",
            imageUrl = null,
            url = "",
        ),
        ArticleUI(
            id = 2,
            title = "2 Use Hilt with other Jetpack libraries",
            description = "2 Hilt includes extensions for providing classes from other Jetpack libraries supports the following Jetpack components.",
            imageUrl = null,
            url = "",
        ),
        ArticleUI(
            id = 3,
            title = "3 Use Hilt with other Jetpack libraries",
            description = "3 Hilt includes extensions for providing classes from other Jetpack libraries supports the following Jetpack components.",
            imageUrl = null,
            url = "",
        ),
    )
}

private class ArticlesPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {

    private val articleProvider = ArticlePreviewProvider()

    override val values = sequenceOf(articleProvider.values.toList())
}
