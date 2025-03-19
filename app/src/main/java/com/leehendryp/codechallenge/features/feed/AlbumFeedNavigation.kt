package com.leehendryp.codechallenge.features.feed

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
internal object AlbumFeedRoute

internal fun NavGraphBuilder.albumFeedGraph() {
    composable<AlbumFeedRoute> {
        val presenter: AlbumFeedPresenter = hiltViewModel()
        AlbumFeedScreen(presenter)
    }
}
