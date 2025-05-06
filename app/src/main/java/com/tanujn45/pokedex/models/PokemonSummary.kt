package com.tanujn45.pokedex.models

data class PokemonSummary(
    val id: Int,
    val name: String,
    val url: String,
    val spriteUrl: String,
    val typeNames: List<String>,
    val speciesColor: String
)
