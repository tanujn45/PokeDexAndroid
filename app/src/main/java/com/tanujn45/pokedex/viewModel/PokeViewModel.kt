package com.tanujn45.pokedex.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanujn45.pokedex.data.PokemonRepository
import com.tanujn45.pokedex.models.PokemonDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface PokeUiState {
    data class Success(val pokemon: PokemonDetail) : PokeUiState
    data object Error : PokeUiState
    data object Loading : PokeUiState
}

class PokeViewModel : ViewModel() {
    private val repo = PokemonRepository()

    var pokeUiState by mutableStateOf<PokeUiState>(PokeUiState.Loading)
        private set

    init {
        fetchPokemon("venusaur")
    }

    private fun fetchPokemon(name: String) {
        viewModelScope.launch {
            pokeUiState = PokeUiState.Loading
            pokeUiState = try {
                val detail = withContext(Dispatchers.IO) {
                    repo.getPokemonDetail(name)
                }
                PokeUiState.Success(detail)
            } catch (e: Exception) {
                PokeUiState.Error
            }
        }
    }
}