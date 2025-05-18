package com.tanujn45.pokedex.models

import com.tanujn45.pokedex.R


val RegionMapDrawables: Map<String, Int> = mapOf(
    "kanto" to R.drawable.kanto_region,
    "johto" to R.drawable.johto_region,
    "hoenn" to R.drawable.hoenn_region,
    "sinnoh" to R.drawable.sinnoh_region,
    "unova" to R.drawable.unova_region,
    "kalos" to R.drawable.kalos_region,
    "alola" to R.drawable.alola_region,
    "galar" to R.drawable.galar_region,
    "hisui" to R.drawable.hisui_region,
    "paldea" to R.drawable.pasio_region,
)

val regionStarters = mapOf(
    "kanto" to listOf("bulbasaur", "charmander", "squirtle"),
    "johto" to listOf("chikorita", "cyndaquil", "totodile"),
    "hoenn" to listOf("treecko", "torchic", "mudkip"),
    "sinnoh" to listOf("turtwig", "chimchar", "piplup"),
    "unova" to listOf("snivy", "tepig", "oshawott"),
    "kalos" to listOf("chespin", "fennekin", "froakie"),
    "alola" to listOf("rowlet", "litten", "popplio"),
    "galar" to listOf("grookey", "scorbunny", "sobble"),
    "hisui" to listOf("wyrdeer", "arctibax", "basculegion"),
    "paldea" to listOf("sprigatito", "fuecoco", "quaxly"),
)
