package com.tanujn45.pokedex.ui.screens.detail


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanujn45.pokedex.models.EvolutionNode
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies
import com.tanujn45.pokedex.models.PokemonType
import com.tanujn45.pokedex.models.bulbasaur
import com.tanujn45.pokedex.models.bulbasaurEvolutions
import com.tanujn45.pokedex.models.bulbasaurSpecies
import com.tanujn45.pokedex.ui.components.Preloader
import com.tanujn45.pokedex.viewModel.FavoritesUiState
import com.tanujn45.pokedex.viewModel.FavoritesViewModel
import com.tanujn45.pokedex.viewModel.PokeUiState
import com.tanujn45.pokedex.viewModel.PokeViewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    pokemonName: String,
    pokemonType: PokemonType = PokemonType.Grass,
    onBack: () -> Unit,
    onPokemonSelected: (String) -> Unit,
    detailViewModel: PokeViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel(),
) {
    BackHandler {
        onBack()
    }

    LaunchedEffect(Unit) {
        detailViewModel.fetchPokemon(pokemonName)
    }

    val detailUiState by detailViewModel.pokeUiState.collectAsState()
    val favoritesUiState by favoritesViewModel.favoritesUiState.collectAsState()


    when (detailUiState) {
        is PokeUiState.Loading -> Preloader(
            color = pokemonType.color,
            modifier = Modifier.fillMaxSize()
        )

        is PokeUiState.Success -> {
            val (pokemon, species, evolutions) = detailUiState as PokeUiState.Success
            val isFavorite = when (favoritesUiState) {
                is FavoritesUiState.Success ->
                    (favoritesUiState as FavoritesUiState.Success)
                        .results
                        .any { it.id == pokemon.id }

                else -> false
            }
            PokemonDetailContent(
                pokemon = pokemon,
                species = species,
                evolutions = evolutions,
                modifier = modifier,
                onBack = onBack,
                isFavorite = isFavorite,
                onFavoriteClick = favoritesViewModel::onToggleFavorite,
                onPokemonSelected = onPokemonSelected,
            )
        }

        is PokeUiState.Error -> Text("Error loading Pokemon details")
    }
}

@Composable
fun PokemonDetailContent(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    pokemon: PokemonDetail,
    species: PokemonSpecies,
    evolutions: EvolutionNode?,
    isPreview: Boolean = false,
    isFavorite: Boolean,
    onFavoriteClick: (id: Int) -> Unit,
    onPokemonSelected: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        PokemonDetailHeader(
            pokemon = pokemon,
            isPreview = isPreview,
            onBack = onBack,
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick
        )
        Spacer(Modifier.height(8.dp))
        PokemonTypeBadges(pokemon.typeSlots)
        Spacer(Modifier.height(16.dp))

        DetailTabContent(
            pokemon = pokemon,
            species = species,
            evolutions = evolutions,
            modifier = Modifier.fillMaxSize(),
            onPokemonSelected = onPokemonSelected,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailPreview() {
    PokemonDetailContent(
        pokemon = bulbasaur,
        species = bulbasaurSpecies,
        evolutions = bulbasaurEvolutions,
        isPreview = true,
        onBack = {},
        isFavorite = false,
        onFavoriteClick = {},
        onPokemonSelected = {}
    )
}

