package com.endeavour.poloaquaticoparedes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.endeavour.poloaquaticoparedes.model.Athlete

@Database(
        entities = [Athlete::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AthleteDatabase : RoomDatabase() {

    abstract fun athleteDao(): AthleteDao

    companion object {

        @Volatile
        private var INSTANCE: AthleteDatabase? = null

        fun getInstance(context: Context): AthleteDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AthleteDatabase::class.java, "Athlete.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}