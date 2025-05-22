package com.leehendryp.codechallenge.features.list.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.leehendryp.codechallenge.R
import com.leehendryp.codechallenge.core.presentation.navigation.Destination
import com.leehendryp.codechallenge.features.list.presentation.ui.AlbumListScreen
import kotlinx.serialization.Serializable

@Serializable
internal data object AlbumList : Destination {
    override val icon = Icons.Filled.LibraryMusic
    override val route = "album_list"
    override val title: Int = R.string.top_bar_title_feed
}

internal fun NavGraphBuilder.albumListGraph(
    snackbarHostState: SnackbarHostState,
    onNavigate: (id: Int) -> Unit,
) {
    composable(AlbumList.route) {
        AlbumListScreen(snackbarHostState = snackbarHostState) { id ->
            onNavigate(id)
        }
    }
}
