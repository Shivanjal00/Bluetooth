package com.example.jaankari.db

import androidx.room.TypeConverter
import com.example.jaankari.models.Source

class Coverters {
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String): Source{
        return Source(name, name)
    }
}