package com.tanujn45.pokedex.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tanujn45.pokedex.data.db.dao.FavoritesDao
import com.tanujn45.pokedex.data.db.dao.PokemonSummaryDao
import com.tanujn45.pokedex.data.db.entities.FavoritesEntity
import com.tanujn45.pokedex.data.db.entities.PokemonSummaryEntity

@Database(
    entities = [PokemonSummaryEntity::class, FavoritesEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val pokemonSummaryDao: PokemonSummaryDao
    abstract val favoritesDao: FavoritesDao
}