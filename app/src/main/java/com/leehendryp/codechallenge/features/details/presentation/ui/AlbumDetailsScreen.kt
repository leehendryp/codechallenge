package com.leehendryp.codechallenge.features.details.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leehendryp.codechallenge.R
import com.leehendryp.codechallenge.features.details.presentation.AlbumDetailsPresenter
import com.leehendryp.codechallenge.features.details.presentation.Intent
import com.leehendryp.codechallenge.features.details.presentation.UIState
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.ui.RetryContent
import com.leehendryp.codechallenge.ui.ds.DSCircularProgressIndicator
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.ThemePreviews

@Composable
internal fun AlbumDetailsScreen(
    modifier: Modifier = Modifier,
    presenter: AlbumDetailsPresenter,
    albumDetailsId: Int,
) {
    val uiState: UIState by presenter.uiState.collectAsStateWithLifecycle()

    AlbumDetailsContent(modifier, uiState) {
        presenter.dispatch(Intent.GetAlbum(albumDetailsId))
    }
}

@Composable
internal fun AlbumDetailsContent(
    modifier: Modifier = Modifier,
    uiState: UIState,
    onIntent: () -> Unit,
) {
    when (uiState) {
        is UIState.LoadingList -> {
            DSCircularProgressIndicator(modifier)

            LaunchedEffect(Unit) {
                onIntent()
            }
        }

        is UIState.Retry -> {
            RetryContent(
                modifier = modifier,
                icon = Icons.Default.Error,
                message = R.string.generic_error,
                hasConnection = true,
            ) {
                onIntent()
            }
        }

        is UIState.Content -> AlbumDetailsItemContent(modifier, uiState.album)
    }
}

@ThemePreviews
@Composable
private fun AlbumDetailsContentPreview() {
    CodeChallengeTheme {
        Surface {
            AlbumDetailsItemContent(
                model = MockDomainModels.mockAlbums.first(),
            )
        }
    }
}
