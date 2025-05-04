package com.tanujn45.pokedex.models

import com.google.gson.annotations.SerializedName

data class EvolutionChain(
    val chain: ChainLink
)

data class ChainLink(
    val species: NamedApiResource, @SerializedName("evolves_to") val evolvesTo: List<ChainLink>
)

fun ChainLink.toEvolutionNode(
    detailMap: Map<String, List<PokemonDetail>>
): EvolutionNode {
    val details = detailMap[species.name] ?: emptyList()
    return EvolutionNode(
        name = details.firstOrNull()?.name ?: species.name,
        details = details,
        children = evolvesTo.map { it.toEvolutionNode(detailMap) })
}

data class EvolutionNode(
    val name: String,
    val details: List<PokemonDetail>,
    val children: List<EvolutionNode> = emptyList()
)

val bulbasaurEvolutions = EvolutionNode(
    name = "bulbasaur",
    details = listOf(bulbasaur),
    children = listOf(
        EvolutionNode(
            name = "ivysaur",
            details = emptyList(),
            children = listOf(
                EvolutionNode(
                    name = "venusaur",
                    details = emptyList(),
                    children = emptyList()
                )
            )
        )
    )
)
