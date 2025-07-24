package com.tanujn45.pokedex.di

import com.tanujn45.pokedex.data.db.dao.FavoritesDao
import com.tanujn45.pokedex.data.db.dao.PokemonSummaryDao
import com.tanujn45.pokedex.data.network.PokeApiService
import com.tanujn45.pokedex.data.repo.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object RepositoryModule {
    @Provides
    @Singleton
    fun providePokemonRepository(
        api: PokeApiService,
        pokemonSummaryDao: PokemonSummaryDao,
        favoritesDao: FavoritesDao
    ): PokemonRepository {
        return PokemonRepository(api, pokemonSummaryDao, favoritesDao)
    }
}