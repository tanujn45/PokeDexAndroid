package com.tanujn45.pokedex.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Search : Screen("search", Icons.Default.Search, "Search")
    object Favorites : Screen("favorites", Icons.Default.Favorite, "Favorites")
    object Locations : Screen("locations", Icons.Default.Place, "Locations")
    object Detail : Screen("detail/{name}", Icons.Default.Info, "Detail") {
        fun createRoute(name: String) = "detail/$name"
    }
}