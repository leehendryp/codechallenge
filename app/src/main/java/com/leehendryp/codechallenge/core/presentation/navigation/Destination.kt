package com.leehendryp.codechallenge.core.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.leehendryp.codechallenge.features.details.presentation.ui.AlbumDetails
import com.leehendryp.codechallenge.features.list.presentation.AlbumList

internal interface Destination {
    // Lee: Useful for deep links, but not strictly necessary for in-app navigation.
    val route: String

    @get:StringRes
    val title: Int

    val icon: ImageVector
}

internal val screens = listOf(
    AlbumList,
    AlbumDetails,
)
