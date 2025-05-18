package com.tanujn45.pokedex.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanujn45.pokedex.data.PokemonRepository
import com.tanujn45.pokedex.models.PokedexEntry
import com.tanujn45.pokedex.models.RegionDetail
import com.tanujn45.pokedex.models.toId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException

class RegionDetailViewModel(
    private val repo: PokemonRepository = PokemonRepository()
) : ViewModel() {

    private val _regionName = MutableStateFlow<String?>(null)
    fun loadRegion(name: String) {
        _regionName.value = name
    }

    val regionDetail: StateFlow<RegionDetail?> = _regionName
        .filterNotNull()
        .flatMapLatest { name ->
            flow { emit(repo.getRegionDetail(name)) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val pokedexEntries: StateFlow<List<PokedexEntry>> = regionDetail
        .filterNotNull()
        .flatMapLatest { detail ->
            flow {
                // ① find a pokédex whose slug exactly matches the region name
                val matching =
                    detail.pokedexes.firstOrNull { it.name.equals(detail.name, ignoreCase = true) }
                // ② otherwise just take the first pokédex in the list
                val dexResource = matching ?: detail.pokedexes.firstOrNull()
                if (dexResource == null) {
                    emit(emptyList())
                } else {
                    val dex = try {
                        val dexId = dexResource.toId()
                        repo.getPokedexDetail(dexId.toString())
                    } catch (_: HttpException) {
                        emit(emptyList())
                        return@flow
                    }
                    emit(dex.pokemonEntries)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val flavorText: StateFlow<String> = pokedexEntries
        .flatMapLatest { list ->
            if (list.isEmpty()) flowOf("")
            else flow { emit(repo.getFlavorTextFor(list.first().pokemonSpecies.name)) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
}
