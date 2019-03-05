package com.endeavour.poloaquaticoparedes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.endeavour.poloaquaticoparedes.model.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(posts: List<Event>)

    @Query("SELECT * FROM events")
    fun allEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE (id LIKE :id)")
    fun eventById(id: Long): LiveData<Event>

    @Query("SELECT * FROM events WHERE (name LIKE :query)")
    fun eventsByName(query: String): LiveData<List<Event>>

    @Update
    fun updateEvent(event: Event)

    @Query("DELETE FROM events WHERE (id LIKE :id)")
    fun deleteEvent(id: Long)

    @Query("DELETE FROM events")
    fun deleteAll()


}