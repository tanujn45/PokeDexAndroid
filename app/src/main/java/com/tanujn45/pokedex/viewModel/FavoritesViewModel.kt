package com.tanujn45.pokedex.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanujn45.pokedex.data.repo.PokemonRepository
import com.tanujn45.pokedex.models.PokemonSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data class Success(val results: List<PokemonSummary>) : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Error(val message: String) : FavoritesUiState
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: PokemonRepository
) : ViewModel() {

    private val _favoritesUiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val favoritesUiState = _favoritesUiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getFavoriteSummaries()
                .onStart { _favoritesUiState.value = FavoritesUiState.Loading }.catch { e ->
                    _favoritesUiState.value = FavoritesUiState.Error(e.message ?: "Unknown error")
                }.collect { list ->
                    _favoritesUiState.value =
                        if (list.isEmpty()) FavoritesUiState.Empty else FavoritesUiState.Success(
                            list
                        )
                }
        }
    }

    fun onToggleFavorite(id: Int) {
        viewModelScope.launch {
            try {
                repo.toggleFavorite(id)
            } catch (e: Exception) {
                _favoritesUiState.value = FavoritesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}