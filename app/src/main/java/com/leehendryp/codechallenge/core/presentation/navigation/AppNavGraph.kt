package com.leehendryp.codechallenge.core.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.leehendryp.codechallenge.features.list.presentation.AlbumListRoute
import com.leehendryp.codechallenge.features.list.presentation.albumListGraph

@Composable
internal fun AppNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AlbumListRoute,
        modifier = modifier,
    ) {
        albumListGraph(snackbarHostState)
    }
}
