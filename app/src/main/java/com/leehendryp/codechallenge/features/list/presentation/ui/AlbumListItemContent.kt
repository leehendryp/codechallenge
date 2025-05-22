package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListItemContentTestTags.ALBUM_LIST_ITEM_CONTENT_IMAGE
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListItemContentTestTags.ALBUM_LIST_ITEM_CONTENT_ROW
import com.leehendryp.codechallenge.ui.ds.DSAsyncImage
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTypography
import com.leehendryp.codechallenge.ui.theme.LocalDimens
import com.leehendryp.codechallenge.ui.theme.ThemePreviews

@Composable
internal fun AlbumListItemContent(
    modifier: Modifier = Modifier,
    model: Album,
    onSelect: () -> Unit,
) {
    val spacing = LocalDimens.current.spacing
    val stroke = LocalDimens.current.stroke
    val colorScheme = MaterialTheme.colorScheme
    val roundedCornerShape = RoundedCornerShape(spacing.xl)
    val imageSize = 120.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(roundedCornerShape)
            .border(
                border = BorderStroke(
                    width = stroke.hairline,
                    color = colorScheme.outline,
                ),
                shape = roundedCornerShape,
            )
            .clickable { onSelect() }
            .padding(spacing.l)
            .testTag(ALBUM_LIST_ITEM_CONTENT_ROW),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DSAsyncImage(
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(spacing.l))
                .background(colorScheme.surfaceContainer)
                .testTag(ALBUM_LIST_ITEM_CONTENT_IMAGE + "_${model.id}"),
            imageUrl = model.thumbnailUrl,
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing.xl),
            text = model.title,
            style = CodeChallengeTypography.titleMedium,
        )
    }
}

@ThemePreviews
@Composable
private fun AlbumContentPreview() {
    CodeChallengeTheme {
        AlbumListItemContent(
            model = MockDomainModels.mockAlbums.first(),
        ) {}
    }
}

internal object AlbumListItemContentTestTags {
    const val ALBUM_LIST_ITEM_CONTENT_ROW = "ALBUM_LIST_ITEM_CONTENT_ROW"
    const val ALBUM_LIST_ITEM_CONTENT_IMAGE = "ALBUM_LIST_ITEM_CONTENT_IMAGE"
}
