package com.tanujn45.pokedex.data

import com.tanujn45.pokedex.models.EvolutionChain
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ): PokemonDetail

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpeciesDetail(
        @Path("name") name: String
    ): PokemonSpecies

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(
        @Path("id") id: Int
    ): EvolutionChain
}
