package com.leehendryp.photoalbum.features.details.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leehendryp.photoalbum.features.common.domain.Album
import com.leehendryp.photoalbum.features.details.presentation.ui.AlbumDetailsItemContentTestTags.ALBUM_DETAILS_ITEM_CONTENT_COLUMN
import com.leehendryp.photoalbum.features.details.presentation.ui.AlbumDetailsItemContentTestTags.ALBUM_DETAILS_ITEM_CONTENT_IMAGE
import com.leehendryp.photoalbum.ui.ds.DSAsyncImage
import com.leehendryp.photoalbum.ui.theme.LocalDimens
import com.leehendryp.photoalbum.ui.theme.PhotoAlbumTypography

@Composable
internal fun AlbumDetailsItemContent(
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
            .padding(spacing.xl)
            .testTag(ALBUM_DETAILS_ITEM_CONTENT_COLUMN),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DSAsyncImage(
            modifier = Modifier
                .padding(bottom = spacing.xl)
                .size(imageSize)
                .clip(RoundedCornerShape(spacing.l))
                .background(colorScheme.surfaceContainer)
                .testTag(ALBUM_DETAILS_ITEM_CONTENT_IMAGE),
            imageUrl = model.url,
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )
        Text(
            text = model.title,
            textAlign = TextAlign.Center,
            style = PhotoAlbumTypography.headlineSmall,
        )
    }
}

internal object AlbumDetailsItemContentTestTags {
    const val ALBUM_DETAILS_ITEM_CONTENT_COLUMN = "ALBUM_DETAILS_ITEM_CONTENT_COLUMN"
    const val ALBUM_DETAILS_ITEM_CONTENT_IMAGE = "ALBUM_DETAILS_CONTENT_IMAGE"
}
