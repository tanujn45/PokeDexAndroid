package com.tanujn45.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonSpecies(
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>,

    @SerializedName("gender_rate") val genderRate: Int,
    val habitat: NamedApiResource?,
    val genera: List<Genus>,
    @SerializedName("evolution_chain") val evolutionChain: UrlApiResource?,
    @SerializedName("varieties") val varieties: List<PokemonSpeciesVariety>,
    @SerializedName("color") val color: NamedApiResource
)

data class FlavorTextEntry(
    @SerializedName("flavor_text") val text: String,
    val language: NamedApiResource,
    val version: NamedApiResource
)

data class Genus(
    @SerializedName("genus") val genus: String, val language: NamedApiResource
)


data class PokemonSpeciesVariety(
    val pokemon: NamedApiResource, @SerializedName("is_default") val isDefault: Boolean
)

fun PokemonSpecies.getEnglishFlavorText(): String =
    flavorTextEntries.lastOrNull { it.language.name == "en" }?.text?.replace('\n', ' ') ?: ""

fun PokemonSpecies.femaleFraction(): Float? = when {
    genderRate < 0 -> null
    else -> genderRate / 8f
}

fun PokemonSpecies.getEnglishGenus(): String =
    genera.lastOrNull { it.language.name == "en" }?.genus ?: ""

val bulbasaurSpecies = PokemonSpecies(
    flavorTextEntries = listOf(
        FlavorTextEntry(
            text = "A strange seed was planted on its back at birth. The plant sprouts and grows with this Pokémon.",
            language = NamedApiResource(name = "en"),
            version = NamedApiResource(name = "red")
        ),
        FlavorTextEntry(
            text = "For some time after its birth, it grows by gaining nourishment from the seed on its back.",
            language = NamedApiResource(name = "en"),
            version = NamedApiResource(name = "yellow")
        )
    ),
    genderRate = 1,
    habitat = NamedApiResource(name = "grassland"),
    genera = listOf(
        Genus(
            genus = "Seed Pokémon",
            language = NamedApiResource(name = "en")
        )
    ),
    evolutionChain = UrlApiResource(
        url = "https://pokeapi.co/api/v2/evolution-chain/1/"
    ), varieties = listOf(
        PokemonSpeciesVariety(
            pokemon = NamedApiResource(name = "bulbasaur"),
            isDefault = true
        )
    ),
    color = NamedApiResource(name = "green")
)