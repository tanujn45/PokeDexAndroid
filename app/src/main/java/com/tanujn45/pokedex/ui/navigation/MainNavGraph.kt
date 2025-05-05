package com.tanujn45.pokedex.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tanujn45.pokedex.ui.components.BottomBar
import com.tanujn45.pokedex.ui.screens.detail.DetailScreen
import com.tanujn45.pokedex.ui.screens.favorites.FavoritesScreen
import com.tanujn45.pokedex.ui.screens.location.LocationScreen
import com.tanujn45.pokedex.ui.screens.search.SearchScreen

@Composable
fun MainNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Search.route) {
                SearchScreen { selectedName ->
                    navController.navigate(Screen.Detail.createRoute(selectedName))
                }
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen { selectedName ->
                    navController.navigate(Screen.Detail.createRoute(selectedName))
                }
            }
            composable(Screen.Locations.route) {
                LocationScreen()
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("name") { type = NavType.StringType })
            ) { backStack ->
                val name = backStack.arguments?.getString("name")!!
                DetailScreen(pokemonName = name, onBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}
