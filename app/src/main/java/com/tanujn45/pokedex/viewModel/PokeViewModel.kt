package com.tanujn45.pokedex.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanujn45.pokedex.data.PokemonRepository
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PokeUiState {
    data class Success(val pokemon: PokemonDetail, val species: PokemonSpecies) : PokeUiState
    data object Error : PokeUiState
    data object Loading : PokeUiState
}

class PokeViewModel : ViewModel() {
    private val repo = PokemonRepository()

    private val _pokeUiState = MutableStateFlow<PokeUiState>(PokeUiState.Loading)
    val pokeUiState = _pokeUiState.asStateFlow()

    init {
        fetchPokemon("lucario")
    }

    private fun fetchPokemon(name: String) {
        viewModelScope.launch {
            _pokeUiState.value = PokeUiState.Loading
            _pokeUiState.value = try {
                val detailDeferred = async(Dispatchers.IO) {
                    repo.getPokemonDetail(name)
                }
                val speciesDeferred = async(Dispatchers.IO) {
                    repo.getPokemonSpecies(name)
                }
                val detail = detailDeferred.await()
                val species = speciesDeferred.await()
                PokeUiState.Success(
                    detail,
                    species
                )
            } catch (e: Exception) {
                PokeUiState.Error
            }
        }
    }
}