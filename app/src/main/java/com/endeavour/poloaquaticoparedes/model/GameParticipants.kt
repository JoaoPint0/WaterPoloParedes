package com.endeavour.poloaquaticoparedes.model

data class GameParticipants (val homeTeamPlayerList:MutableList<GamePerson>,
                             val awayTeamPlayerList:MutableList<GamePerson>,
                             val homeTeamCoachList:MutableList<GamePerson>,
                             val awayTeamCoachList:MutableList<GamePerson>,
                             val refereeList:MutableList<GamePerson>)