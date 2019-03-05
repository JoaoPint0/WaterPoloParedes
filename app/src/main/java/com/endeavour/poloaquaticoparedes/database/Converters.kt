package com.endeavour.poloaquaticoparedes.database

import androidx.room.TypeConverter
import com.endeavour.poloaquaticoparedes.model.Leagues
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return (date?.time)
    }

    @TypeConverter
    fun toLeague(league: Leagues?): String? {
        return (league?.name)
    }

    @TypeConverter
    fun leagueToString(name: String?): Leagues? {
        return if (name.isNullOrBlank()) null else Leagues.valueOf(name.toUpperCase())
    }

    @TypeConverter
    fun leaguesToJSON(leagues: List<Leagues>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Leagues>>() {}.type
        return gson.toJson(leagues, type)
    }

    @TypeConverter
    fun jsonToLeagues(json: String): List<Leagues> {
        val gson = Gson()
        val type = object : TypeToken<List<Leagues>>() {}.type
        return gson.fromJson(json, type)
    }
}