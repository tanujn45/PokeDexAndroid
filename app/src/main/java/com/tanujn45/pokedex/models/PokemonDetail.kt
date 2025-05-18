package com.tanujn45.pokedex.models

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    val id: Int, val name: String,
    @SerializedName("base_experience") val baseExperience: Int,
    @SerializedName("height") val weight: Int,
    @SerializedName("weight") val height: Int,
    @SerializedName("sprites") val sprites: Sprites,
    @SerializedName("types") val typeSlots: List<TypeSlot>,
    @SerializedName("abilities") val abilitySlots: List<AbilitySlot>,
    @SerializedName("moves") val moveSlots: List<MoveSlot>,
    @SerializedName("cries") val cries: Cries,
    @SerializedName("species") val species: NamedApiResource,
    @SerializedName("stats") val statSlots: List<StatSlot>,
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?,

    @SerializedName("other") val other: OtherSprites
)

data class OtherSprites(
    @SerializedName("dream_world") val dreamWorld: DreamWorldSprites,
    @SerializedName("showdown") val showdown: ShowdownSprites
)

data class DreamWorldSprites(
    @SerializedName("front_default") val spriteUrl: String?
)

data class ShowdownSprites(
    @SerializedName("front_default") val spriteUrl: String?
)

data class TypeSlot(
    @SerializedName("type") val type: NamedApiResource
)

data class AbilitySlot(
    @SerializedName("ability") val ability: NamedApiResource
)

data class MoveSlot(
    val move: NamedApiResource,
    @SerializedName("version_group_details")
    val versionGroupDetails: List<PokemonMoveVersion>
)

data class PokemonMoveVersion(
    @SerializedName("move_learn_method")
    val moveLearnMethod: NamedApiResource,
    @SerializedName("version_group")
    val versionGroup: NamedApiResource,
    @SerializedName("level_learned_at")
    val levelLearnedAt: Int,
    val order: Int
)

data class StatSlot(
    @SerializedName("base_stat") val baseStat: Int,

    @SerializedName("stat") val stat: NamedApiResource
)

data class Cries(
    @SerializedName("latest") val latest: String,

    @SerializedName("legacy") val legacy: String
)


fun PokemonDetail.getSpriteUrl(): String =
    sprites.other.dreamWorld.spriteUrl ?: sprites.frontDefault ?: ""

val bulbasaur = PokemonDetail(
    id = 1, name = "Bulbasaur", height = 7, weight = 69, sprites = Sprites(
        frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
        other = OtherSprites(
            dreamWorld = DreamWorldSprites(
                spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/1.svg"
            ), showdown = ShowdownSprites(
                spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/1.gif"
            )
        )
    ), typeSlots = listOf(
        TypeSlot(NamedApiResource("grass")), TypeSlot(NamedApiResource("poison"))
    ), abilitySlots = listOf(
        AbilitySlot(NamedApiResource("overgrow")), AbilitySlot(NamedApiResource("chlorophyll"))
    ), statSlots = listOf(
        StatSlot(baseStat = 45, stat = NamedApiResource("hp")),
        StatSlot(baseStat = 49, stat = NamedApiResource("attack")),
        StatSlot(baseStat = 49, stat = NamedApiResource("defense")),
        StatSlot(baseStat = 65, stat = NamedApiResource("special-attack")),
        StatSlot(baseStat = 65, stat = NamedApiResource("special-defense")),
        StatSlot(baseStat = 45, stat = NamedApiResource("speed"))
    ), baseExperience = 100, species = NamedApiResource("bulbasaur"),
    moveSlots = listOf(
        MoveSlot(
            move = NamedApiResource("razor-wind"),
            versionGroupDetails = listOf(
                PokemonMoveVersion(
                    moveLearnMethod = NamedApiResource("egg"),
                    versionGroup = NamedApiResource("red-blue"),
                    levelLearnedAt = 0,
                    order = 1
                )
            )
        )
    ),
    cries = Cries(
        latest = "",
        legacy = "",
    )
)
