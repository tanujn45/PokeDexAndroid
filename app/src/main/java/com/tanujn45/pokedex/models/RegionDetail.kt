package com.tanujn45.pokedex.models

import com.google.gson.annotations.SerializedName

data class RegionDetail(
    val id: Int,
    val name: String,
    val names: List<Name>,
    @SerializedName("main_generation")
    val mainGeneration: NamedApiResource,
    @SerializedName("version_groups")
    val versionGroups: List<NamedApiResource>,
    val pokedexes: List<NamedUrlApiResource>,
    val locations: List<NamedUrlApiResource>
)

data class Name(
    val name: String,
    val language: NamedApiResource
)
