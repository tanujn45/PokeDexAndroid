package com.tanujn45.pokedex.data

import com.tanujn45.pokedex.models.ChainLink
import com.tanujn45.pokedex.models.EvolutionNode
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies
import com.tanujn45.pokedex.models.toEvolutionNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class PokemonRepository {
    private val api = RetrofitInstance.api

    suspend fun getPokemonDetail(name: String): PokemonDetail {
        return api.getPokemonDetail(name)
    }

    suspend fun getPokemonSpecies(name: String): PokemonSpecies {
        return api.getPokemonSpeciesDetail(name)
    }

    suspend fun getEvolutionTree(species: PokemonSpecies): EvolutionNode? =
        withContext(Dispatchers.IO) {
            val chainId = extractChainId(species) ?: return@withContext null

            val evoChain = api.getEvolutionChain(chainId)

            val allNames = collectAllSpeciesNames(evoChain.chain)

            val detailMap = fetchAllVarietyDetails(allNames)

            evoChain.chain.toEvolutionNode(detailMap)
        }

    private fun extractChainId(species: PokemonSpecies): Int? {
        val url = species.evolutionChain?.url ?: return null
        return url
            .trimEnd('/')
            .substringAfterLast('/')
            .toIntOrNull()
    }

    private fun collectAllSpeciesNames(root: ChainLink): Set<String> {
        val names = mutableSetOf<String>()
        fun recurse(link: ChainLink) {
            names += link.species.name
            link.evolvesTo.forEach { recurse(it) }
        }
        recurse(root)
        return names
    }

    private suspend fun allVarietyNamesForSpecies(speciesName: String): List<String> {
        val species = api.getPokemonSpeciesDetail(speciesName)
        // The species’ ‘varieties’ list contains every form; pull out their .pokemon.name
        return species.varieties.map { it.pokemon.name }
    }

    private suspend fun fetchAllVarietyDetails(
        speciesNames: Set<String>
    ): Map<String, List<PokemonDetail>> = coroutineScope {
        speciesNames.map { sp ->
            async(Dispatchers.IO) {
                // 1) find every variety name for this species
                val varietyNames = allVarietyNamesForSpecies(sp)
                // 2) fetch each PokemonDetail for each variety
                val details = varietyNames.map { pokeName ->
                    api.getPokemonDetail(pokeName)
                }
                sp to details
            }
        }.awaitAll().toMap()
    }
}
