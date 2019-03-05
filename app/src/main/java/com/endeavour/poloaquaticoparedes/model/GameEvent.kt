package com.endeavour.poloaquaticoparedes.model

data class GameEvent(val type: GameEventType,
                val team: Long,
                val playerNumber: Int,
                val period:Int,
                val time: Long)
