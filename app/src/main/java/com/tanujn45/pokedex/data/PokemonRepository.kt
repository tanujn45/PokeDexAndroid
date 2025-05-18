package com.tanujn45.pokedex.data

import com.tanujn45.pokedex.models.ChainLink
import com.tanujn45.pokedex.models.EvolutionNode
import com.tanujn45.pokedex.models.FavoritesEntity
import com.tanujn45.pokedex.models.NamedUrlApiResource
import com.tanujn45.pokedex.models.PokedexDetail
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies
import com.tanujn45.pokedex.models.PokemonSummary
import com.tanujn45.pokedex.models.PokemonSummaryEntity
import com.tanujn45.pokedex.models.RegionDetail
import com.tanujn45.pokedex.models.getEnglishFlavorText
import com.tanujn45.pokedex.models.getSpriteUrl
import com.tanujn45.pokedex.models.toEvolutionNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PokemonRepository {
    private val api = RetrofitInstance.api
    private val pokemonSummaryDao = AppDatabase.get().pokemonSummaryDao
    private val favoritesDao = AppDatabase.get().favoritesDao

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
        return url.trimEnd('/').substringAfterLast('/').toIntOrNull()
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

    private suspend fun getAllPokemon(limit: Int, offset: Int): List<NamedUrlApiResource> {
        return api.getPokemonList(limit, offset).results
    }

    suspend fun refreshPokemonSummaries() {
        val list = getAllPokemon(1000, 0)
        val entities = coroutineScope {
            list.map { resource ->
                async {
                    // parallel fetch
                    val detail = api.getPokemonDetail(resource.name)
                    val speciesName = detail.species.name
                    val species = api.getPokemonSpeciesDetail(speciesName)
                    PokemonSummaryEntity(
                        id = detail.id,
                        name = resource.name,
                        url = resource.url,
                        spriteUrl = detail.getSpriteUrl(),
                        typeNames = detail.typeSlots.map { it.type.name },
                        speciesColor = species.color.name
                    )
                }
            }.awaitAll()
        }
        pokemonSummaryDao.insertAll(entities)
    }

    fun searchPokemonByName(query: String): Flow<List<PokemonSummary>> {
        return pokemonSummaryDao.searchByName("%$query%").map { entities ->
            entities.map {
                PokemonSummary(
                    id = it.id,
                    name = it.name,
                    url = it.url,
                    spriteUrl = it.spriteUrl,
                    typeNames = it.typeNames,
                    speciesColor = it.speciesColor
                )
            }
        }
    }

    fun getAllPokemonSummaries(): Flow<List<PokemonSummary>> {
        return pokemonSummaryDao.getAll().map { entities ->
            entities.map {
                PokemonSummary(
                    id = it.id,
                    name = it.name,
                    url = it.url,
                    spriteUrl = it.spriteUrl,
                    typeNames = it.typeNames,
                    speciesColor = it.speciesColor
                )
            }
        }
    }

    fun getFavoriteSummaries(): Flow<List<PokemonSummary>> =
        favoritesDao.getAll().combine(pokemonSummaryDao.getAll()) { faves, all ->
            val ids = faves.map { it.id }.toSet()
            all.filter { it.id in ids }
        }.map { entities ->
            entities.map { ent ->
                PokemonSummary(
                    id = ent.id,
                    name = ent.name,
                    url = ent.url,
                    spriteUrl = ent.spriteUrl,
                    typeNames = ent.typeNames,
                    speciesColor = ent.speciesColor
                )
            }
        }

    suspend fun toggleFavorite(id: Int) {
        val isFav = favoritesDao.getAll().first().any { it.id == id }
        if (isFav) favoritesDao.remove(id)
        else favoritesDao.add(FavoritesEntity(id))
    }

    suspend fun getRegionDetail(name: String): RegionDetail =
        api.getPokemonRegion(name)

    suspend fun getPokedexEntries(dexName: String): List<String> =
        api.getPokedexDetail(dexName).pokemonEntries.map { it.pokemonSpecies.name }

    suspend fun getPokedexDetail(name: String): PokedexDetail =
        api.getPokedexDetail(name)

    suspend fun getFlavorTextFor(speciesName: String): String {
        val species = getPokemonSpecies(speciesName)
        return species.getEnglishFlavorText()
    }
}
