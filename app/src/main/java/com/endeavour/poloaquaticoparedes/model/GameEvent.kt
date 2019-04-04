package com.endeavour.poloaquaticoparedes.model

data class GameEvent(
    val type: GameEventType,
    val team: Int,
    val playerName: String,
    val period: Int,
    val time: Long
) {
    constructor() : this(
        GameEventType.TIME_OUT,
        0,
        "",
        0,
        1L)
}