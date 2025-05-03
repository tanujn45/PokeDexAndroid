package com.tanujn45.pokedex.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.tanujn45.pokedex.R

enum class PokemonType(val displayName: String, @DrawableRes val iconRes: Int, val color: Color) {
    Bug("Bug", R.drawable.bug, Color(0xFF729F3F)),
    Dark("Dark", R.drawable.dark, Color(0xFF707070)),
    Dragon("Dragon", R.drawable.dragon, Color(0xFF0F6AC0)),
    Electric("Electric", R.drawable.electric, Color(0xFFF8D030)),
    Fairy("Fairy", R.drawable.fairy, Color(0xFFD685AD)),
    Fighting("Fighting", R.drawable.fighting, Color(0xFFC03028)),
    Fire("Fire", R.drawable.fire, Color(0xFFF08030)),
    Flying("Flying", R.drawable.flying, Color(0xFFA890F0)),
    Ghost("Ghost", R.drawable.ghost, Color(0xFF705898)),
    Grass("Grass", R.drawable.grass, Color(0xFF78C850)),
    Ground("Ground", R.drawable.ground, Color(0xFFA4733C)),
    Ice("Ice", R.drawable.ice, Color(0xFF98D8D8)),
    Normal("Normal", R.drawable.normal, Color(0xFFA8A878)),
    Poison("Poison", R.drawable.poison, Color(0xFFA040A0)),
    Psychic("Psychic", R.drawable.psychic, Color(0xFFF85888)),
    Rock("Rock", R.drawable.rock, Color(0xFFB8A038)),
    Steel("Steel", R.drawable.steel, Color(0xFFB8B8D0)),
    Water("Water", R.drawable.water, Color(0xFF6890F0));

    companion object {
        fun fromString(type: String): PokemonType? {
            return values().find { it.displayName.equals(type, ignoreCase = true) }
        }
    }
}
