package com.endeavour.poloaquaticoparedes.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

data class Game(
    @PrimaryKey var id: Long,
    var homeTeam: Team ,
    var awayTeam: Team ,
    @Ignore
    val participants: GameParticipants,
    var target: Leagues,
    var date: Date,
    var competition: String,
    var local: String,
    var round: Int,
    var time : Long,
    @Ignore
    val activity: MutableList<GameEvent>
)