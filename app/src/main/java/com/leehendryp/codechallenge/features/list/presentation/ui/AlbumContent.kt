package com.leehendryp.codechallenge.features.list.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.MockDomainModels
import com.leehendryp.codechallenge.ui.ds.DSAsyncImage
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTypography
import com.leehendryp.codechallenge.ui.theme.ThemePreviews

@Composable
internal fun AlbumContent(
    modifier: Modifier = Modifier,
    model: Album,
) {
    val colorScheme = MaterialTheme.colorScheme
    val roundedCornerShape = RoundedCornerShape(24.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(roundedCornerShape)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = colorScheme.outline,
                ),
                shape = roundedCornerShape,
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DSAsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorScheme.surfaceContainer),
            imageUrl = model.thumbnailUrl,
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            text = model.title,
            style = CodeChallengeTypography.titleMedium,
        )
    }
}

@ThemePreviews
@Composable
private fun AlbumContentPreview() {
    CodeChallengeTheme {
        AlbumContent(
            model = MockDomainModels.mockAlbums.first(),
        )
    }
}
