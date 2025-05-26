package com.leehendryp.photoalbum.features.details.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leehendryp.photoalbum.R
import com.leehendryp.photoalbum.features.common.domain.MockDomainModels
import com.leehendryp.photoalbum.features.details.presentation.AlbumDetailsPresenter
import com.leehendryp.photoalbum.features.details.presentation.Intent
import com.leehendryp.photoalbum.features.details.presentation.UIState
import com.leehendryp.photoalbum.features.list.presentation.ui.RetryContent
import com.leehendryp.photoalbum.ui.ds.DSCircularProgressIndicator
import com.leehendryp.photoalbum.ui.theme.PhotoAlbumTheme
import com.leehendryp.photoalbum.ui.theme.ThemePreviews

@Composable
internal fun AlbumDetailsScreen(
    modifier: Modifier = Modifier,
    albumDetailsId: Int,
    presenter: AlbumDetailsPresenter = hiltViewModel(),
) {
    val uiState: UIState by presenter.uiState.collectAsStateWithLifecycle()

    AlbumDetailsContent(modifier, uiState) {
        presenter.dispatch(Intent.GetAlbum(albumDetailsId))
    }
}

@Composable
private fun AlbumDetailsContent(
    modifier: Modifier = Modifier,
    uiState: UIState,
    onIntent: () -> Unit,
) {
    when (uiState) {
        is UIState.Loading -> {
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
    PhotoAlbumTheme {
        Surface {
            AlbumDetailsItemContent(
                model = MockDomainModels.mockAlbums.first(),
            )
        }
    }
}
