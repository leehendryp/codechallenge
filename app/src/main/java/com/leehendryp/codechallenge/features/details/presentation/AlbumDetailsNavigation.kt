package com.leehendryp.codechallenge.features.details.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.leehendryp.codechallenge.R
import com.leehendryp.codechallenge.core.presentation.navigation.Destination
import com.leehendryp.codechallenge.features.details.presentation.AlbumDetails.ALBUM_DETAILS_ID
import com.leehendryp.codechallenge.features.details.presentation.ui.AlbumDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
internal data object AlbumDetails : Destination {
    override val icon = Icons.Filled.MusicNote
    override val route = "details"
    override val title: Int = R.string.top_bar_title_details

    internal val routeWithArgs = "$route/{$ALBUM_DETAILS_ID}"

    internal val arguments = listOf(
        navArgument(ALBUM_DETAILS_ID) { type = NavType.IntType },
    )

    internal const val ALBUM_DETAILS_ID = "id"
}

internal fun NavController.toDetails(id: Int) = navigate("${AlbumDetails.route}/$id")

internal fun NavGraphBuilder.albumDetailsGraph() {
    composable(
        route = AlbumDetails.routeWithArgs,
        arguments = AlbumDetails.arguments,
    ) { backStackEntry ->

        backStackEntry.arguments?.getInt(ALBUM_DETAILS_ID)?.let { id ->
            AlbumDetailsScreen(albumDetailsId = id)
        }
    }
}
