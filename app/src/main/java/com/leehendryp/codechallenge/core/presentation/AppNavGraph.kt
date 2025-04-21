package com.leehendryp.codechallenge.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.leehendryp.codechallenge.features.list.presentation.AlbumListRoute
import com.leehendryp.codechallenge.features.list.presentation.albumListGraph

@Composable
internal fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AlbumListRoute) {
        albumListGraph()
    }
}
