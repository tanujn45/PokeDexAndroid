package com.tanujn45.pokedex.data.network

import com.tanujn45.pokedex.models.EvolutionChain
import com.tanujn45.pokedex.models.PokedexDetail
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonList
import com.tanujn45.pokedex.models.PokemonSpecies
import com.tanujn45.pokedex.models.RegionDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int, @Query("offset") offset: Int
    ): PokemonList

    @GET("region/{name}")
    suspend fun getPokemonRegion(
        @Path("name") name: String
    ): RegionDetail

    @GET("pokedex/{name}")
    suspend fun getPokedexDetail(
        @Path("name") name: String
    ): PokedexDetail
}
