package com.tanujn45.pokedex.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tanujn45.pokedex.models.PokemonSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonSummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(summaries: List<PokemonSummaryEntity>)

    @Query(
        """
        SELECT * FROM pokemon_summary
        WHERE name LIKE '%' || :query || '%' 
        ORDER BY name
    """
    )
    fun searchByName(query: String): Flow<List<PokemonSummaryEntity>>

    @Query("SELECT * FROM pokemon_summary ORDER BY id")
    fun getAll(): Flow<List<PokemonSummaryEntity>>
}