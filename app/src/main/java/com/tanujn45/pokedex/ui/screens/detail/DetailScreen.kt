package com.tanujn45.pokedex.ui.screens.detail


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.bulbasaur
import com.tanujn45.pokedex.viewModel.PokeUiState
import com.tanujn45.pokedex.viewModel.PokeViewModel

@Composable
fun PokemonDetailScreen(
    modifier: Modifier = Modifier, viewModel: PokeViewModel = viewModel()
) {
    when (val pokeUiState = viewModel.pokeUiState) {
        is PokeUiState.Loading -> Text("Loading...")
        is PokeUiState.Success -> {
            val pokemon = pokeUiState.pokemon
            PokemonDetailContent(pokemon = pokemon, modifier = modifier)
        }

        is PokeUiState.Error -> Text("Error loading Pokemon details")
    }
}

@Composable
fun PokemonDetailContent(
    modifier: Modifier = Modifier, pokemon: PokemonDetail, isPreview: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        PokemonDetailHeader(pokemon = pokemon, isPreview = isPreview)
        PokemonTypeBadges(pokemon.typeSlots)
        DetailTabContent(pokemon = pokemon)
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailPreview() {
    PokemonDetailContent(pokemon = bulbasaur, isPreview = true)
}

