package com.endeavour.poloaquaticoparedes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.endeavour.poloaquaticoparedes.model.Athlete

@Dao
interface AthleteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(posts: List<Athlete>)

    @Query("SELECT * FROM athletes")
    fun allAthletes(): LiveData<List<Athlete>>

    @Query("SELECT * FROM athletes WHERE (cardId LIKE :id) ")
    fun athleteByCardId(id: Long): LiveData<Athlete>

    @Update
    fun updateAthlete(athlete: Athlete)

    @Query("SELECT * FROM athletes WHERE (cardId LIKE :query) OR (name LIKE :query) OR (level LIKE :query)")
    fun athletesByNameCardIdOrLevel(query: String): LiveData<List<Athlete>>

    @Query("DELETE FROM athletes")
    fun deleteAll()

    @Query("DELETE FROM athletes WHERE (cardId LIKE :cardId)")
    fun deleteAthlete(cardId: String)

}