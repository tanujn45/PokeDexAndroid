package com.tanujn45.pokedex.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tanujn45.pokedex.data.db.dao.FavoritesDao
import com.tanujn45.pokedex.data.db.dao.PokemonSummaryDao
import com.tanujn45.pokedex.models.ChainLink
import com.tanujn45.pokedex.models.EvolutionNode
import com.tanujn45.pokedex.data.db.entities.FavoritesEntity
import com.tanujn45.pokedex.models.PokedexDetail
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies
import com.tanujn45.pokedex.models.PokemonSummary
import com.tanujn45.pokedex.data.db.entities.PokemonSummaryEntity
import com.tanujn45.pokedex.data.network.PokeApiService
import com.tanujn45.pokedex.models.RegionDetail
import com.tanujn45.pokedex.models.getEnglishFlavorText
import com.tanujn45.pokedex.models.getSpriteUrl
import com.tanujn45.pokedex.models.toEvolutionNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: PokeApiService,
    private val pokemonSummaryDao: PokemonSummaryDao,
    private val favoritesDao: FavoritesDao
) {

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

    suspend fun getRegionDetail(name: String): RegionDetail = api.getPokemonRegion(name)

    suspend fun getPokedexDetail(name: String): PokedexDetail = api.getPokedexDetail(name)

    suspend fun getFlavorTextFor(speciesName: String): String {
        val species = getPokemonSpecies(speciesName)
        return species.getEnglishFlavorText()
    }

    suspend fun preloadAllPokemonSummaries() = withContext(Dispatchers.IO) {
        val pageSize = 100
        var offset = 0
        val allDone = false

        while (!allDone) {
            val response = api.getPokemonList(limit = pageSize, offset = offset)

            val resources = response.results
            if (resources.isEmpty()) {
                break
            }

            val summaries = resources.map { resource ->
                async {
                    val detail = api.getPokemonDetail(resource.name)
                    val species = api.getPokemonSpeciesDetail(detail.species.name)

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

            pokemonSummaryDao.insertAll(summaries)

            offset += pageSize

            delay(100)
        }
    }


    suspend fun isPokemonSummaryEmpty(): Boolean {
        return pokemonSummaryDao.getCount() == 0
    }


    fun getPagedPokemonSummaries(query: String): Flow<PagingData<PokemonSummary>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, enablePlaceholders = false
            ), pagingSourceFactory = {
                if (query.isBlank()) {
                    pokemonSummaryDao.pagingSource()
                } else {
                    pokemonSummaryDao.searchPagingSource(query)
                }
            }).flow.map { pagingData ->
            pagingData.map { entity ->
                PokemonSummary(
                    id = entity.id,
                    name = entity.name,
                    url = entity.url,
                    spriteUrl = entity.spriteUrl,
                    typeNames = entity.typeNames,
                    speciesColor = entity.speciesColor
                )
            }
        }
    }
}
