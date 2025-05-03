package com.tanujn45.pokedex.data

import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies

class PokemonRepository {
    private val api = RetrofitInstance.api

    suspend fun getPokemonDetail(name: String): PokemonDetail {
       return api.getPokemonDetail(name)
    }

    suspend fun getPokemonSpecies(name: String): PokemonSpecies {
        return api.getPokemonSpeciesDetail(name)
    }
}
