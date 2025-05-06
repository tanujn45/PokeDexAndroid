package com.tanujn45.pokedex.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? = list?.joinToString(separator = ",")

    @TypeConverter
    fun toStringList(data: String?): List<String> =
        data?.takeIf { it.isNotBlank() }?.split(",") ?: emptyList()
}