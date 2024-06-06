package com.demyanchikpolina.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.demyanchikpolina.news.NewsSearchTheme

@Composable
fun NewsMainScreen() {
    NewsMainScreen(viewModel = viewModel())
}
@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()

    if (state != State.None) {
        NewsMainContent(state)
    }
}

@Composable
private fun NewsMainContent(state: State) {
    Column {
        if (state is State.Error) {
            ErrorMessage(state)
        }
        if (state is State.Loading) {
            ProgressIndicator(state)
        }
        state.articles?.let { Articles(articles = it) }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
private fun ProgressIndicator(state: State) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
private fun ErrorMessage(state: State) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NewsSearchTheme.colorScheme.error)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error occurred during update",
            color = NewsSearchTheme.colorScheme.onError
        )
    }
}

@Preview
@Composable
private fun Articles(
    @PreviewParameter(ArticlesPreviewProvider::class) articles: List<ArticleUI>
) {
    LazyColumn (contentPadding = PaddingValues(4.dp)) {
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
    Row (modifier = Modifier.padding(bottom = 4.dp)) {
        article.imageUrl?.let { imageUrl ->

            var isImageVisible by remember { mutableStateOf(true) }

            if (isImageVisible) {
                AsyncImage(
                    model = imageUrl,
                    onState = { state ->
                        if (state is AsyncImagePainter.State.Error) {
                            isImageVisible = false
                        }
                    },
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.title,
                style = NewsSearchTheme.typography.headlineMedium,
                maxLines = 1
            )
            article.description?.let {
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = it,
                    style = NewsSearchTheme.typography.bodyMedium,
                    maxLines = 3
                )
            }
        }
    }
}

private class ArticlePreviewProvider : PreviewParameterProvider<ArticleUI> {
    override val values = sequenceOf(
        ArticleUI(
            id = 1,
            title = "1 Use Hilt with other Jetpack libraries",
            description = "1 Hilt includes extensions for providing classes from" +
                    " other Jetpack libraries supports the following Jetpack components.",
            imageUrl = null,
            url = "",
        ),
        ArticleUI(
            id = 2,
            title = "2 Use Hilt with other Jetpack libraries",
            description = "2 Hilt includes extensions for providing classes from" +
                    " other Jetpack libraries supports the following Jetpack components.",
            imageUrl = null,
            url = "",
        ),
        ArticleUI(
            id = 3,
            title = "3 Use Hilt with other Jetpack libraries",
            description = "3 Hilt includes extensions for providing classes from other" +
                    " Jetpack libraries supports the following Jetpack components.",
            imageUrl = null,
            url = "",
        ),
    )
}

private class ArticlesPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {

    private val articleProvider = ArticlePreviewProvider()

    override val values = sequenceOf(articleProvider.values.toList())
}
