package com.tanujn45.pokedex.model

import androidx.annotation.DrawableRes

data class MockPokemon(
    val id: Int,
    val name: String,
    val types: List<PokemonType>,
    @DrawableRes val imageResourceId: Int,
)
