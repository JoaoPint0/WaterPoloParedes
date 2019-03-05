package com.endeavour.poloaquaticoparedes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "events")
data class Event(
    @PrimaryKey var id: Long,
    val isGame: Boolean,
    val name: String,
    val picture: String,
    val location: String,
    val date: Date,
    val priority: Long,
    val target: List<Leagues>,
    val content: String
)