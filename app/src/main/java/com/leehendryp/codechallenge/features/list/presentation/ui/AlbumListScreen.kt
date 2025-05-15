package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.leehendryp.codechallenge.R
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.AlbumListPresenter
import com.leehendryp.codechallenge.features.list.presentation.Intent
import com.leehendryp.codechallenge.features.list.presentation.UIState
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.ALBUM_LIST_LAZY_COLUMN
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.FULL_SCREEN_PROGRESS_INDICATOR
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.PAGING_PROGRESS_INDICATOR
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.TOP_APP_BAR
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.ThemePreviews
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun AlbumListScreen(
    modifier: Modifier = Modifier,
    presenter: AlbumListPresenter,
) {
    val uiState: UIState by presenter.uiState.collectAsStateWithLifecycle()

    AlbumListScreenContent(modifier, uiState) { intent ->
        presenter.dispatch(intent)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AlbumListScreenContent(
    modifier: Modifier = Modifier,
    uiState: UIState,
    onIntent: (Intent) -> Unit,
) {
    LaunchedEffect(Unit) {
        onIntent(Intent.GetAlbums)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    uiState.snackbar?.let { snackBar ->
        val message = when (snackBar) {
            UIState.Snackbar.Error -> stringResource(R.string.feed_error)
        }

        LaunchedEffect(snackBar) {
            snackbarHostState.showSnackbar(message)
        }
    }

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val status = uiState.status) {
                is UIState.Status.LoadingList -> FullScreenLoadingWheel()

                is UIState.Status.Content -> AlbumListContent(
                    status = status,
                    onIntent = onIntent,
                )

                is UIState.Status.ClientRetry -> RetryContent(
                    icon = Icons.Default.WifiOff,
                    message = R.string.feed_retry_client_issue,
                    hasConnection = uiState.hasConnection,
                ) {
                    onIntent(Intent.GetAlbums)
                }

                is UIState.Status.ServerRetry -> RetryContent(
                    icon = Icons.Default.Close,
                    message = R.string.feed_retry_server_issue,
                    hasConnection = uiState.hasConnection,
                ) {
                    onIntent(Intent.GetAlbums)
                }

                is UIState.Status.Empty -> RetryContent(
                    icon = Icons.Default.DataArray,
                    message = R.string.feed_empty,
                    hasConnection = uiState.hasConnection,
                ) {
                    onIntent(Intent.GetAlbums)
                }
            }
        }
    }
}

@Composable
private fun AlbumListContent(
    status: UIState.Status.Content,
    onIntent: (Intent) -> Unit,
) {
    val lazyPagingItems = status.pagingDataFlow.collectAsLazyPagingItems()

    // Show full screen loading for initial refresh
    when (val loadState = lazyPagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            FullScreenLoadingWheel()
            return
        }

        is LoadState.Error -> onIntent(Intent.HandleError(loadState.error))
        else -> Unit
    }

    HandleEmptyListOnCompleteSourceLoading(lazyPagingItems, onIntent)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(ALBUM_LIST_LAZY_COLUMN),
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = { index -> lazyPagingItems[index]?.id ?: index },
        ) { index ->
            lazyPagingItems[index]?.let { album ->
                AlbumContent(model = album)
            }
        }

        // Show bottom-loading spinner for pagination
        when (val appendState = lazyPagingItems.loadState.append) {
            is LoadState.Loading -> item {
                PagingLoadingWheel()
            }

            is LoadState.Error -> onIntent(Intent.HandleError(appendState.error))
            else -> Unit
        }
    }
}

@Composable
private fun FullScreenLoadingWheel(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier.testTag(FULL_SCREEN_PROGRESS_INDICATOR))
}

@Composable
private fun PagingLoadingWheel() {
    Box(
        modifier = Modifier
            .testTag(PAGING_PROGRESS_INDICATOR)
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}


@Composable
private fun HandleEmptyListOnCompleteSourceLoading(
    lazyPagingItems: LazyPagingItems<Album>,
    onIntent: (Intent) -> Unit,
) {
    val isEmpty = with(lazyPagingItems.loadState) {
        source.refresh is LoadState.NotLoading &&
                append.endOfPaginationReached &&
                lazyPagingItems.itemCount == 0
    }

    LaunchedEffect(isEmpty) {
        if (isEmpty) {
            onIntent(Intent.HandleNoItems)
        }
    }
}

@ThemePreviews
@Composable
private fun AlbumListContentPreview() {
    CodeChallengeTheme {
        val pagingDataFlow = flowOf(
            PagingData.from(
                data = MockDomainModels.mockAlbums,
                sourceLoadStates =
                    LoadStates(
                        refresh = LoadState.NotLoading(false),
                        append = LoadState.NotLoading(false),
                        prepend = LoadState.NotLoading(false),
                    ),
            ),
        )

        AlbumListScreenContent(
            uiState = UIState(
                status = UIState.Status.Content(
                    pagingDataFlow = pagingDataFlow,
                ),
                hasConnection = true,
            ),
        ) {}
    }
}

@ThemePreviews
@Composable
private fun EmptyContentPreview() {
    CodeChallengeTheme {
        AlbumListScreenContent(
            uiState = UIState(
                status = UIState.Status.Empty,
                hasConnection = true,
            ),
        ) {}
    }
}

internal object AlbumListScreenTestTags {
    const val TOP_APP_BAR = "TOP_BAR"
    const val FULL_SCREEN_PROGRESS_INDICATOR = "FULL_SCREEN_PROGRESS_INDICATOR"
    const val PAGING_PROGRESS_INDICATOR = "PAGING_PROGRESS_INDICATOR"
    const val ALBUM_LIST_LAZY_COLUMN = "ALBUM_LIST_LAZY_COLUMN"
}
