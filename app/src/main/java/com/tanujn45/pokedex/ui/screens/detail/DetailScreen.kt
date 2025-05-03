package com.tanujn45.pokedex.ui.screens.detail


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies
import com.tanujn45.pokedex.models.bulbasaur
import com.tanujn45.pokedex.models.bulbasaurSpecies
import com.tanujn45.pokedex.viewModel.PokeUiState
import com.tanujn45.pokedex.viewModel.PokeViewModel

@Composable
fun PokemonDetailScreen(
    modifier: Modifier = Modifier, viewModel: PokeViewModel = viewModel()
) {
    val uiState by viewModel.pokeUiState.collectAsState()
    when (uiState) {
        is PokeUiState.Loading -> Text("Loading...")
        is PokeUiState.Success -> {
            val (pokemon, species) = uiState as PokeUiState.Success
            PokemonDetailContent(pokemon = pokemon, species = species, modifier = modifier)
        }

        is PokeUiState.Error -> Text("Error loading Pokemon details")
    }
}

@Composable
fun PokemonDetailContent(
    modifier: Modifier = Modifier,
    pokemon: PokemonDetail,
    species: PokemonSpecies,
    isPreview: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()            // bound the height
            .padding(16.dp)
    ) {
        // Header & type badges stay at the top (non-scrolling)
        PokemonDetailHeader(pokemon = pokemon, isPreview = isPreview)
        Spacer(Modifier.height(8.dp))
        PokemonTypeBadges(pokemon.typeSlots)
        Spacer(Modifier.height(16.dp))

        DetailTabContent(
            pokemon = pokemon,
            species = species,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PokemonDetailPreview() {
    PokemonDetailContent(pokemon = bulbasaur, species = bulbasaurSpecies, isPreview = true)
}

