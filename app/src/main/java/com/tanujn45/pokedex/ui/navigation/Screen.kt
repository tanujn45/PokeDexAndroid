package com.tanujn45.pokedex.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Search : Screen("search", Icons.Outlined.Search, "Search")
    object Favorites : Screen("favorites", Icons.Outlined.FavoriteBorder, "Favorites")
    object Locations : Screen("locations", Icons.Outlined.Place, "Locations")
    object Detail : Screen("detail/{name}", Icons.Outlined.Info, "Detail") {
        fun createRoute(name: String) = "detail/$name"
    }
}