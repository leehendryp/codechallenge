package com.leehendryp.codechallenge.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.leehendryp.codechallenge.core.presentation.navigation.AppNavGraph
import com.leehendryp.codechallenge.core.presentation.navigation.Destination
import com.leehendryp.codechallenge.core.presentation.navigation.screens
import com.leehendryp.codechallenge.features.list.presentation.AlbumList
import com.leehendryp.codechallenge.ui.theme.CodeChallengeTheme
import com.leehendryp.codechallenge.ui.theme.LocalDimens
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeChallengeTheme {
                CompositionLocalProvider(LocalLifecycleOwner provides this) {
                    CodeChallengeApp()
                }
            }
        }
    }

    @Composable
    private fun CodeChallengeApp() {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = screens.find { it.matchesRoute(currentDestination?.route) } ?: AlbumList
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = {
                Crossfade(currentScreen) { currentScreen ->
                    TopAppBar(
                        title = { TopBarTitle(currentScreen) },
                    )
                }
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
