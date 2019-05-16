package com.endeavour.poloaquaticoparedes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.endeavour.poloaquaticoparedes.model.Athlete
import com.endeavour.poloaquaticoparedes.model.Event
import com.endeavour.poloaquaticoparedes.model.Team

@Database(entities = [Team::class, Event::class, Athlete::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PoloDatabase : RoomDatabase() {

    abstract fun teamDao(): TeamDao
    abstract fun eventDao(): EventDao
    abstract fun athleteDao(): AthleteDao

    companion object {

        @Volatile
        private var INSTANCE: PoloDatabase? = null

        fun getInstance(context: Context): PoloDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                PoloDatabase::class.java, "Polo.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}