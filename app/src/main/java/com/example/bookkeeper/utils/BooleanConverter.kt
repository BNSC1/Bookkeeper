package com.example.bookkeeper.utils

import androidx.room.TypeConverter

object BooleanConverter {
    @TypeConverter
    @JvmStatic
    fun toBoolean(value: Int): Boolean {
        return value == 1
    }

    @TypeConverter
    @JvmStatic
    fun fromBoolean(value: Boolean): Int {
        return if (value) 1 else 0
    }
}