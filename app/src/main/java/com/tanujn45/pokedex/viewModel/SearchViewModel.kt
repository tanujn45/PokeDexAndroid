package com.tanujn45.pokedex.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tanujn45.pokedex.data.repo.PokemonRepository
import com.tanujn45.pokedex.models.PokemonSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: PokemonRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val pokemonPagingFlow: StateFlow<PagingData<PokemonSummary>> =
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                repo.getPagedPokemonSummaries(query).cachedIn(viewModelScope)
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    init {
        viewModelScope.launch {
            if (repo.isPokemonSummaryEmpty()) {
                repo.preloadAllPokemonSummaries()
            }
        }
    }

    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
