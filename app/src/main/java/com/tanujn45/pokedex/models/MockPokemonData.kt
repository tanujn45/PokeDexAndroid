package com.tanujn45.pokedex.models

import com.tanujn45.pokedex.R

val mockPokemonList = listOf(
    MockPokemon(25, "Pikachu", listOf(PokemonType.Electric), R.drawable.pikachu_25),
    MockPokemon(1, "Bulbasaur", listOf(PokemonType.Grass, PokemonType.Poison), R.drawable.bulbasaur_1),
    MockPokemon(4, "Charmander", listOf(PokemonType.Fire), R.drawable.charmander_4),
    MockPokemon(7, "Squirtle", listOf(PokemonType.Water), R.drawable.squirtle_7),
    MockPokemon(39, "Jigglypuff", listOf(PokemonType.Fairy, PokemonType.Normal), R.drawable.jigglypuff_39),
    MockPokemon(143, "Snorlax", listOf(PokemonType.Normal), R.drawable.snorlax_143),
    MockPokemon(94, "Gengar", listOf(PokemonType.Ghost, PokemonType.Poison), R.drawable.gengar_94),
    MockPokemon(149, "Dragonite", listOf(PokemonType.Dragon, PokemonType.Flying), R.drawable.dragonite_149),
)
