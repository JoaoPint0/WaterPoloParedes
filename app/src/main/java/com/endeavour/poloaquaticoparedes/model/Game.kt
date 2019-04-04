package com.endeavour.poloaquaticoparedes.model

import androidx.room.PrimaryKey
import java.util.*

data class Game(
    @PrimaryKey var id: String,
    val home: Team,
    val away: Team,
    val target: Leagues,
    val date: Date,
    val competition: String,
    val local: String,
    var round: Int,
    var time : Long,
    val referees: MutableList<String>,
    val activity: MutableList<GameEvent>
) {
    constructor() : this(
        "",
        Team("", "", "", mutableListOf(), mutableListOf()),
        Team("", "", "", mutableListOf(), mutableListOf()),
        Leagues.SENIOR_MALE,
        Date(),
        "",
        "",
        1,
        0L,
        mutableListOf(),
        mutableListOf())
}