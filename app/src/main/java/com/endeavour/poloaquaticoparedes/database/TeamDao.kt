package com.endeavour.poloaquaticoparedes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.endeavour.poloaquaticoparedes.model.Team

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(posts: List<Team>)

    @Query("SELECT * FROM teams")
    fun allTeams(): LiveData<List<Team>>

    @Query("DELETE FROM teams")
    fun deleteAll()
}