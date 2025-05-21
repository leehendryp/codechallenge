package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
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
import com.leehendryp.codechallenge.features.list.presentation.UISideEffect
import com.leehendryp.codechallenge.features.list.presentation.UIState
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.ALBUM_LIST_LAZY_COLUMN
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreenTestTags.PAGING_PROGRESS_INDICATOR
import com.leehendryp.codechallenge.ui.ds.DSCircularProgressIndicator
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.LocalDimens
import com.leehendryp.codechallenge.ui.theme.ThemePreviews
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun AlbumListScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    presenter: AlbumListPresenter,
    onNavigate: (id: Int) -> Unit,
) {
    val uiState: UIState by presenter.uiState.collectAsStateWithLifecycle()

    AlbumListScreenContent(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
    ) { intent ->
        presenter.dispatch(intent)
    }

    LaunchedEffect(Unit) {
        presenter.uiSideEffect.collect { effect ->
            when (effect) {
                is UISideEffect.GoToDetails -> onNavigate(effect.id)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AlbumListScreenContent(
    modifier: Modifier = Modifier,
    uiState: UIState,
    snackbarHostState: SnackbarHostState,
    onIntent: (Intent) -> Unit,
) {
    uiState.snackbar?.let { snackBar ->
        val message = when (snackBar) {
            UIState.Snackbar.Error -> stringResource(R.string.generic_error)
        }

        LaunchedEffect(snackBar) {
            snackbarHostState.showSnackbar(message)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (val status = uiState.status) {
            is UIState.Status.LoadingList -> {
                DSCircularProgressIndicator()

                LaunchedEffect(Unit) {
                    onIntent(Intent.GetAlbums)
                }
            }

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

@Composable
private fun AlbumListContent(
    status: UIState.Status.Content,
    onIntent: (Intent) -> Unit,
) {
    val spacing = LocalDimens.current.spacing
    val lazyPagingItems = status.pagingDataFlow.collectAsLazyPagingItems()

    if (showLoadingOnRefreshState(lazyPagingItems.loadState, onIntent)) return

    HandleEmptyListOnCompleteSourceLoading(lazyPagingItems, onIntent)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(ALBUM_LIST_LAZY_COLUMN),
        contentPadding = PaddingValues(vertical = spacing.xl, horizontal = spacing.l),
        verticalArrangement = Arrangement.spacedBy(spacing.l),
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = { index -> lazyPagingItems[index]?.id ?: index },
        ) { index ->
            lazyPagingItems[index]?.let { album ->
                AlbumContent(model = album) {
                    onIntent(Intent.SeeDetails(album.id))
                }
            }
        }

        handleAppendState(lazyPagingItems.loadState, onIntent)
    }
}

@Composable
private fun showLoadingOnRefreshState(
    loadStates: CombinedLoadStates,
    onIntent: (Intent) -> Unit,
): Boolean {
    when (val loadState = loadStates.refresh) {
        is LoadState.Loading -> {
            DSCircularProgressIndicator()
            return true
        }

        is LoadState.Error -> onIntent(Intent.HandleError(loadState.error))
        else -> Unit
    }
    return false
}

private fun LazyListScope.handleAppendState(
    loadStates: CombinedLoadStates,
    onIntent: (Intent) -> Unit,
) {
    when (val appendState = loadStates.append) {
        is LoadState.Loading -> item {
            PagingLoadingWheel()
        }

        is LoadState.Error -> onIntent(Intent.HandleError(appendState.error))
        else -> Unit
    }
}

@Composable
private fun PagingLoadingWheel() {
    val spacing = LocalDimens.current.spacing

    Box(
        modifier = Modifier
            .testTag(PAGING_PROGRESS_INDICATOR)
            .fillMaxWidth()
            .padding(vertical = spacing.l),
        contentAlignment = Alignment.Center,
    ) {
        DSCircularProgressIndicator()
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
            snackbarHostState = SnackbarHostState(),
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
            snackbarHostState = SnackbarHostState(),
        ) {}
    }
}

internal object AlbumListScreenTestTags {
    const val TOP_APP_BAR = "TOP_BAR"
    const val PAGING_PROGRESS_INDICATOR = "PAGING_PROGRESS_INDICATOR"
    const val ALBUM_LIST_LAZY_COLUMN = "ALBUM_LIST_LAZY_COLUMN"
}
