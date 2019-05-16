package com.endeavour.poloaquaticoparedes.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "events")
data class Event(
    @PrimaryKey var id: Long,
    var publicEvent: Boolean,
    var name: String,
    var picture: String,
    var location: String,
    var date: Date,
    var priority: Long,
    val target: List<Leagues>,
    var content: String
){
    constructor() : this(0, false, "", "","", Date(),0L, mutableListOf(Leagues.SENIOR_MALE), "")
}