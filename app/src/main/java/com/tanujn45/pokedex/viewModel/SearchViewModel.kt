package com.tanujn45.pokedex.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanujn45.pokedex.data.PokemonRepository
import com.tanujn45.pokedex.models.PokemonSummary
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface SearchUiState {
    data class Idle(val results: List<PokemonSummary>) : SearchUiState
    object Loading : SearchUiState
    data class Success(val results: List<PokemonSummary>) : SearchUiState
    object Empty : SearchUiState
    data class Error(val message: String) : SearchUiState
}

class SearchViewModel : ViewModel() {
    private val repo = PokemonRepository()

    private val _searchUiState =
        MutableStateFlow<SearchUiState>(SearchUiState.Idle(results = emptyList()))
    val searchUiState = _searchUiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            repo.refreshPokemonSummaries()
            repo.getAllPokemonSummaries().collect { list ->
                _searchUiState.value = SearchUiState.Idle(results = list)
            }
        }
    }

    fun onQueryChanged(query: String) {
        val trimmed = query.trim()

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)

            _searchUiState.value = SearchUiState.Loading

            if (trimmed.isEmpty()) {
                kotlin.runCatching {
                    repo.searchPokemonByName("")
                        .first()
                }.fold(
                    onSuccess = { list ->
                        _searchUiState.value =
                            if (list.isEmpty()) SearchUiState.Empty
                            else SearchUiState.Idle(results = list)
                    },
                    onFailure = { err ->
                        _searchUiState.value = SearchUiState.Error(err.message.orEmpty())
                    }
                )
            } else {
                repo.searchPokemonByName(trimmed)
                    .catch { e ->
                        _searchUiState.value = SearchUiState.Error(e.message.orEmpty())
                    }
                    .collect { list ->
                        _searchUiState.value =
                            if (list.isEmpty()) SearchUiState.Empty
                            else SearchUiState.Success(results = list)
                    }
            }
        }
    }
}