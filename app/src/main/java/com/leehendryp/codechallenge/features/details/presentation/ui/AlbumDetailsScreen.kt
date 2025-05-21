package com.leehendryp.codechallenge.features.details.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun AlbumDetailsScreen(
    modifier: Modifier = Modifier,
    albumDetailsId: Int,
) {
    Column(modifier) {
        Text(
            text = albumDetailsId.toString(),
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
