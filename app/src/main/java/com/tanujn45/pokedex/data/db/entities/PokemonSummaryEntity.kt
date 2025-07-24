package com.tanujn45.pokedex.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_summary")
data class PokemonSummaryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val url: String,
    val spriteUrl: String,
    val typeNames: List<String>,
    val speciesColor: String
)
