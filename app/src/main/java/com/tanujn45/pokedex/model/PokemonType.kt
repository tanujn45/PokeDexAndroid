package com.tanujn45.pokedex.model

import androidx.compose.ui.graphics.Color

enum class PokemonType(val displayName: String, val color: Color) {
    Normal("Normal", Color(0xFFA8A77A)), Fire("Fire", Color(0xFFEE8130)), Water(
        "Water",
        Color(0xFF6390F0)
    ),
    Electric("Electric", Color(0xFFF7D02C)), Grass("Grass", Color(0xFF7AC74C)), Ice(
        "Ice",
        Color(0xFF96D9D6)
    ),
    Fighting("Fighting", Color(0xFFC22E28)), Poison("Poison", Color(0xFFA33EA1)), Ground(
        "Ground",
        Color(0xFFE2BF65)
    ),
    Flying("Flying", Color(0xFFA98FF3)), Psychic("Psychic", Color(0xFFF95587)), Bug(
        "Bug",
        Color(0xFFA6B91A)
    ),
    Rock("Rock", Color(0xFFB6A136)), Ghost("Ghost", Color(0xFF735797)), Dragon(
        "Dragon",
        Color(0xFF6F35FC)
    ),
    Dark("Dark", Color(0xFF705746)), Steel("Steel", Color(0xFFB7B7CE)), Fairy(
        "Fairy",
        Color(0xFFD685AD)
    );

    companion object {
        fun fromString(value: String): PokemonType? =
            entries.find { it.name.equals(value, ignoreCase = true) }
    }
}
