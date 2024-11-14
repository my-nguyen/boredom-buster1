package eu.maxkim.boredombuster1.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import eu.maxkim.boredombuster1.activity.ui.favorite.FavoritesScreen
import eu.maxkim.boredombuster1.activity.ui.newactivity.NewActivity
import eu.maxkim.boredombuster1.model.Destination

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destination.Home.path
    ) {
        navigation(
            startDestination = Destination.Activity.path,
            route = Destination.Home.path
        ) {
            composable(Destination.Activity.path) {
                NewActivity(modifier = modifier)
            }
            composable(Destination.Favorites.path) {
                FavoritesScreen(modifier = modifier)
            }
        }
    }
}