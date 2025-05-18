package com.tanujn45.pokedex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tanujn45.pokedex.ui.navigation.Screen

@Composable
fun FloatingBottomBar(
    modifier: Modifier = Modifier.padding(30.dp),
    navController: NavController
) {
    val items = listOf(Screen.Search, Screen.Favorites, Screen.Locations)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            shadowElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { screen ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 36.dp)
                            .fillMaxHeight()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            tint = if (currentRoute == screen.route) Color.Black else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
