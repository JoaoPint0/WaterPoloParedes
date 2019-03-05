package com.endeavour.poloaquaticoparedes.model

import androidx.room.PrimaryKey
import java.util.*

data class Game(
    @PrimaryKey var id: Long,
    val home: Team,
    val away: Team,
    val target: List<Leagues>,
    val date: Date,
    val competition: String,
    val local: String,
    val referees: List<String>,
    val activity: List<GameEvent>
)