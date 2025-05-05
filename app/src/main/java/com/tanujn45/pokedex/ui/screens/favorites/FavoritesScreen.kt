package com.tanujn45.pokedex.ui.screens.favorites

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onPokemonSelected: (String) -> Unit
) {
    Text(
        text = "Favorites",
        modifier = modifier
    )
}