package com.tanujn45.pokedex.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tanujn45.pokedex.data.db.entities.PokemonSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonSummaryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

    @Query("SELECT COUNT(*) FROM pokemon_summary")
    suspend fun getCount(): Int

    @Query("SELECT * FROM pokemon_summary ORDER BY id")
    fun pagingSource(): PagingSource<Int, PokemonSummaryEntity>

    @Query("SELECT * FROM pokemon_summary WHERE name LIKE '%' || :query || '%' ORDER BY name")
    fun searchPagingSource(query: String): PagingSource<Int, PokemonSummaryEntity>
}