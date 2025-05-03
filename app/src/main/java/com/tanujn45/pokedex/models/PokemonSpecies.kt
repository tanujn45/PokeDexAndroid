package com.tanujn45.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonSpecies(
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,

    @SerializedName("gender_rate")
    val genderRate: Int,       // 0–8 (or -1 if genderless)
    val habitat: NamedApiResource?,
    val genera: List<Genus>
)

data class FlavorTextEntry(
    @SerializedName("flavor_text") val text: String,
    val language: NamedApiResource,
    val version: NamedApiResource
)

data class Genus(
    @SerializedName("genus") val genus: String,
    val language: NamedApiResource
)