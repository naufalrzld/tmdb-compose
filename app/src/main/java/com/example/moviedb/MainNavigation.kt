package com.example.moviedb

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.moviedb.ui.detail.MovieDetailScreen
import com.example.moviedb.ui.list.MoviesScreen
import kotlinx.serialization.Serializable

@Serializable
object MoviesScreen

@Serializable
data class MovieDetailScreen(val movieId: Int)

@Composable
fun MainNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MoviesScreen,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                animationSpec = tween(300, easing = EaseInOut),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            slideOutOfContainer(
                animationSpec = tween(300, easing = EaseInOut),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                animationSpec = tween(300, easing = EaseInOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                animationSpec = tween(300, easing = EaseInOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {
        composable<MoviesScreen> {
            MoviesScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }

        composable<MovieDetailScreen> {
            val args = it.toRoute<MovieDetailScreen>()
            MovieDetailScreen(
                movieId = args.movieId,
                snackbarHostState = snackbarHostState
            )
        }
    }
}