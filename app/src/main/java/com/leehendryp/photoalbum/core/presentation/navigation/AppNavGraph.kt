package com.leehendryp.photoalbum.core.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.leehendryp.photoalbum.features.details.presentation.albumDetailsGraph
import com.leehendryp.photoalbum.features.details.presentation.toDetails
import com.leehendryp.photoalbum.features.list.presentation.AlbumList
import com.leehendryp.photoalbum.features.list.presentation.albumListGraph

@Composable
internal fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(
        navController = navController,
        startDestination = AlbumList.route,
        modifier = modifier,
    ) {
        albumListGraph(snackbarHostState) { id ->
            navController.toDetails(id)
        }
        albumDetailsGraph()
    }
}
