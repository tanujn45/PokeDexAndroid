package com.tanujn45.pokedex.data

import com.tanujn45.pokedex.models.PokemonDetail

class PokemonRepository {
    private val api = RetrofitInstance.api

    suspend fun getPokemonDetail(name: String): PokemonDetail {
       return api.getPokemonDetail(name)
    }
}
