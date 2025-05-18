package com.tanujn45.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokedexDetail(
    @SerializedName("pokemon_entries")
    val pokemonEntries: List<PokedexEntry>
)

data class PokedexEntry(
    @SerializedName("entry_number")
    val entryNumber: Int,

    @SerializedName("pokemon_species")
    val pokemonSpecies: NamedApiResource
)
