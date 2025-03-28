package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leehendryp.codechallenge.R
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.AlbumListPresenter
import com.leehendryp.codechallenge.features.list.presentation.Intent
import com.leehendryp.codechallenge.features.list.presentation.UIState
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.ALBUM_LIST_LAZY_COLUMN
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.EMPTY_STATE
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.PROGRESS_INDICATOR
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.TOP_APP_BAR
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.ThemePreviews

@Composable
internal fun AlbumListScreen(
    modifier: Modifier = Modifier,
    presenter: AlbumListPresenter,
) {
    val uiState: UIState by presenter.uiState.collectAsStateWithLifecycle()
    AlbumListContent(modifier, uiState) { intent ->
        presenter.dispatch(intent)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AlbumListContent(
    modifier: Modifier = Modifier,
    uiState: UIState,
    onIntent: (Intent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag(TOP_APP_BAR),
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = {
                    Text(stringResource(R.string.feed_title_albums))
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val status = uiState.status) {
                UIState.Status.LoadingList -> {
                    CircularProgressIndicator(modifier = Modifier.testTag(PROGRESS_INDICATOR))
                    onIntent(Intent.FetchData)
                }

                is UIState.Status.Content -> AlbumListLazyColumn(status)
                UIState.Status.Empty -> Text(
                    modifier = Modifier.testTag(EMPTY_STATE),
                    text = stringResource(R.string.feed_message_empty_state),
                )
            }
        }
    }
}

@Composable
private fun AlbumListLazyColumn(status: UIState.Status.Content) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(ALBUM_LIST_LAZY_COLUMN),
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = status.models,
            key = { it.id },
        ) { album ->
            AlbumContent(model = album)
        }
    }
}

@ThemePreviews
@Composable
private fun AlbumListContentPreview() {
    CodeChallengeTheme {
        AlbumListContent(
            uiState = UIState(
                status = UIState.Status.Content(
                    models = MockDomainModels.mockAlbums,
                ),
            ),
        ) {}
    }
}

internal object AlbumListScreenTestTags {
    const val TOP_APP_BAR = "TOP_BAR"
    const val PROGRESS_INDICATOR = "PROGRESS_INDICATOR_TAG"
    const val ALBUM_LIST_LAZY_COLUMN = "ALBUM_LIST_LAZY_COLUMN_TAG"
    const val EMPTY_STATE = "EMPTY_STATE_TAG"
}
