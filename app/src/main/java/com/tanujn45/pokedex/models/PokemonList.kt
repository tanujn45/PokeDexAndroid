package com.tanujn45.pokedex.models

data class PokemonList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ListNamedApiResource>
)

data class ListNamedApiResource(
    val name: String,
    val url: String
)
