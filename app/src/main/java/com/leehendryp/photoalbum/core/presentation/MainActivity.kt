package com.leehendryp.photoalbum.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.leehendryp.photoalbum.R
import com.leehendryp.photoalbum.core.presentation.navigation.AppNavGraph
import com.leehendryp.photoalbum.core.presentation.navigation.Destination
import com.leehendryp.photoalbum.core.presentation.navigation.screens
import com.leehendryp.photoalbum.features.list.presentation.AlbumList
import com.leehendryp.photoalbum.ui.theme.LocalDimens
import com.leehendryp.photoalbum.ui.theme.PhotoAlbumTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoAlbumTheme {
                CompositionLocalProvider(LocalLifecycleOwner provides this) {
                    PhotoAlbumApp()
                }
            }
        }
    }

    @Composable
    private fun PhotoAlbumApp() {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = screens.find { it.matchesRoute(currentDestination?.route) } ?: AlbumList
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { TopBarTitle(currentScreen) },
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            BackIconButton(isVisible = currentScreen != AlbumList) {
                                navController.navigateUp()
                            }
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            AppNavGraph(
                navController = navController,
                snackbarHostState = snackbarHostState,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }

    @Composable
    private fun BackIconButton(isVisible: Boolean, onClick: () -> Unit) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally(),
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.top_bar_back_button_content_description),
                )
            }
        }
    }

    @Composable
    private fun TopBarTitle(currentScreen: Destination) {
        val spacing = LocalDimens.current.spacing

        Row {
            Icon(
                modifier = Modifier.padding(horizontal = spacing.s),
                imageVector = currentScreen.icon,
                contentDescription = null,
            )
            Text(stringResource(currentScreen.title))
        }
    }
}
