package com.leehendryp.codechallenge.features.list.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreen
import kotlinx.serialization.Serializable

@Serializable
internal object AlbumListRoute

internal fun NavGraphBuilder.albumListGraph() {
    composable<AlbumListRoute> {
        val presenter: AlbumListPresenter = hiltViewModel()
        AlbumListScreen(presenter = presenter)
    }
}
