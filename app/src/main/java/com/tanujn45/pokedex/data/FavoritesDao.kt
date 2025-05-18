package com.tanujn45.pokedex.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tanujn45.pokedex.models.FavoritesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites_entity")
    fun getAll(): Flow<List<FavoritesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(fav: FavoritesEntity)

    @Query("DELETE FROM favorites_entity WHERE id = :id")
    suspend fun remove(id: Int)
}