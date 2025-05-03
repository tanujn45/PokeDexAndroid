package com.tanujn45.pokedex.data

import com.tanujn45.pokedex.models.PokemonDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ): PokemonDetail
}
