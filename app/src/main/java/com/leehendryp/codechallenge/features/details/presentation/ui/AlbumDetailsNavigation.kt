package com.leehendryp.codechallenge.features.details.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.leehendryp.codechallenge.R
import com.leehendryp.codechallenge.core.presentation.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
internal data object AlbumDetails : Destination {
    override val icon = Icons.Filled.MusicNote
    override val route = "details"
    override val title: Int = R.string.top_bar_title_details
}

fun NavController.toDetails() = navigate(AlbumDetails)

internal fun NavGraphBuilder.albumDetailsGraph() {
    composable<AlbumDetails> {
        AlbumDetailsScreen()
    }
}
