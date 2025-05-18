package com.tanujn45.pokedex.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_entity")
data class FavoritesEntity(
    @PrimaryKey val id: Int,
)
