package com.tanujn45.pokedex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tanujn45.pokedex.models.PokemonSummaryEntity

@Database(
    entities = [PokemonSummaryEntity::class], version = 3, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val pokemonSummaryDao: PokemonSummaryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java, "pokemon_db"
                    ).fallbackToDestructiveMigration(false).build()
                }
            }
        }

        fun get(): AppDatabase {
            return INSTANCE ?: throw IllegalStateException("Database not initialized")
        }
    }
}