package com.tanujn45.pokedex.di

import android.content.Context
import androidx.room.Room
import com.tanujn45.pokedex.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pokemon_db"
        ).build()
    }

    @Provides
    fun providePokemonSummaryDao(appDatabase: AppDatabase) = appDatabase.pokemonSummaryDao

    @Provides
    fun provideFavoritesDao(appDatabase: AppDatabase) = appDatabase.favoritesDao

}