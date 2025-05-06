package com.tanujn45.pokedex.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isDetailScreen = currentRoute?.startsWith(Screen.Detail.route.substringBefore("{")) ?: false

    Log.d("Navigation", "Current route: $currentRoute, isDetailScreen: $isDetailScreen")
    Scaffold(
//        topBar = {
//            // Force a TopAppBar to always be visible for debugging
//            val isDetailScreen = currentRoute == Screen.Detail.route
//            Log.d("Navigation", "isDetailScreen: $isDetailScreen")
//
//            if (isDetailScreen) {
//                val name = navBackStackEntry?.arguments?.getString("name") ?: "Unknown"
//                Log.d("Navigation", "Name from args: $name")
//                DetailTopAppBar(name = name, onBack = { navController.popBackStack() })
//            }
//        },
        modifier = modifier,
        bottomBar = { BottomBar(navController) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Search.route) {
                SearchScreen(onPokemonSelected = { selectedName ->
                    navController.navigate(Screen.Detail.createRoute(selectedName))
                })
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
