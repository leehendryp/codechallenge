package com.leehendryp.codechallenge.features.details.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leehendryp.codechallenge.R
import com.leehendryp.codechallenge.features.details.presentation.AlbumDetailsPresenter
import com.leehendryp.codechallenge.features.details.presentation.Intent
import com.leehendryp.codechallenge.features.details.presentation.UIState
import com.leehendryp.codechallenge.features.details.presentation.ui.AlbumDetailsContentTestTags.ALBUM_DETAILS_CONTENT_IMAGE
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.ui.RetryContent
import com.leehendryp.codechallenge.ui.ds.DSAsyncImage
import com.leehendryp.codechallenge.ui.ds.DSCircularProgressIndicator
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTypography
import com.leehendryp.codechallenge.ui.theme.LocalDimens
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
private fun AlbumDetailsContent(
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

        is UIState.Content -> AlbumContent(modifier, uiState.album)
    }
}

@Composable
internal fun AlbumContent(
    modifier: Modifier = Modifier,
    model: Album,
) {
    val spacing = LocalDimens.current.spacing
    val colorScheme = MaterialTheme.colorScheme
    val roundedCornerShape = RoundedCornerShape(spacing.xl)
    val imageSize = 240.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(roundedCornerShape)
            .padding(spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DSAsyncImage(
            modifier = Modifier
                .padding(bottom = spacing.xl)
                .size(imageSize)
                .clip(RoundedCornerShape(spacing.l))
                .background(colorScheme.surfaceContainer)
                .testTag(ALBUM_DETAILS_CONTENT_IMAGE),
            imageUrl = model.url,
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )
        Text(
            text = model.title,
            textAlign = TextAlign.Center,
            style = CodeChallengeTypography.headlineSmall,
        )
    }
}

@ThemePreviews
@Composable
private fun AlbumDetailsContentPreview() {
    CodeChallengeTheme {
        Surface {
            AlbumContent(
                model = MockDomainModels.mockAlbums.first(),
            )
        }
    }
}

internal object AlbumDetailsContentTestTags {
    const val ALBUM_DETAILS_CONTENT_IMAGE = "ALBUM_DETAILS_CONTENT_IMAGE"
}
