package com.leehendryp.codechallenge.core.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.leehendryp.codechallenge.features.details.presentation.AlbumDetails
import com.leehendryp.codechallenge.features.list.presentation.AlbumList

internal interface Destination {
    // Lee: Useful for dynamic routes and deep links, but not strictly needed for in-app navigation.
    val route: String

    @get:StringRes
    val title: Int

    val icon: ImageVector

    fun matchesRoute(currentRoute: String?): Boolean = currentRoute?.startsWith(route) == true
}

internal val screens = listOf(
    AlbumList,
    AlbumDetails,
)
