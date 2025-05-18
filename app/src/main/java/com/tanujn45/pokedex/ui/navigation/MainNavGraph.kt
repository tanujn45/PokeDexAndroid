package com.tanujn45.pokedex.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tanujn45.pokedex.ui.screens.detail.DetailScreen
import com.tanujn45.pokedex.ui.screens.favorites.FavoritesScreen
import com.tanujn45.pokedex.ui.screens.region.RegionDetailScreen
import com.tanujn45.pokedex.ui.screens.search.SearchScreen

@Composable
fun MainNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//    val isDetailScreen = currentRoute?.startsWith(Screen.Detail.route.substringBefore("{")) ?: false

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateLeftPadding(
                    layoutDirection = LayoutDirection.Ltr
                ),
                end = innerPadding.calculateRightPadding(
                    layoutDirection = LayoutDirection.Ltr
                ),
                bottom = 0.dp
            )
        ) {
            composable(Screen.Search.route) {
                SearchScreen(
                    onPokemonSelected = { selectedName ->
                        navController.navigate(Screen.Detail.createRoute(selectedName))
                    },
                    navController = navController,
                    bottomPadding = innerPadding.calculateBottomPadding()
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onPokemonSelected = { selectedName ->
                        navController.navigate(Screen.Detail.createRoute(selectedName))
                    },
                    navController = navController,
                    bottomPadding = innerPadding.calculateBottomPadding()
                )
            }
            composable(Screen.Locations.route) {
                RegionDetailScreen(
                    regionName = "kanto",
                    navController = navController,
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("name") { type = NavType.StringType })
            ) { backStack ->
                val name = backStack.arguments?.getString("name")!!
                DetailScreen(
                    pokemonName = name,
                    onBack = {
                        navController.popBackStack()
                    },
                    onPokemonSelected = { selectedName ->
                        navController.navigate(
                            Screen.Detail.createRoute(selectedName)
                        )
                    }
                )
            }
        }
    }
}

