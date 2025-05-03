package com.tanujn45.pokedex.models

import androidx.annotation.DrawableRes

data class MockPokemon(
    val id: Int,
    val name: String,
    val types: List<PokemonType>,
    @DrawableRes val imageResourceId: Int,
)
