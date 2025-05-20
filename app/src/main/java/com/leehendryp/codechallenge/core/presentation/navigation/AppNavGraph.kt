package com.leehendryp.codechallenge.core.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.leehendryp.codechallenge.features.details.presentation.ui.albumDetailsGraph
import com.leehendryp.codechallenge.features.details.presentation.ui.toDetails
import com.leehendryp.codechallenge.features.list.presentation.AlbumList
import com.leehendryp.codechallenge.features.list.presentation.albumListGraph

@Composable
internal fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(
        navController = navController,
        startDestination = AlbumList,
        modifier = modifier,
    ) {
        albumListGraph(snackbarHostState) {
            navController.toDetails()
        }
        albumDetailsGraph()
    }
}
