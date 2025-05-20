package com.leehendryp.codechallenge.core.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.ui.graphics.vector.ImageVector
import com.leehendryp.codechallenge.R

sealed interface Destination {
    val icon: ImageVector
    val route: String

    @get:StringRes
    val title: Int
}

data object AlbumList : Destination {
    override val icon = Icons.Filled.LibraryMusic
    override val route = "album_list"
    override val title: Int = R.string.top_bar_title_feed
}

data object AlbumDetails : Destination {
    override val icon = Icons.Filled.MusicNote
    override val route = "details"
    override val title: Int = R.string.top_bar_title_details
}

val screens = listOf(AlbumList, AlbumDetails)
