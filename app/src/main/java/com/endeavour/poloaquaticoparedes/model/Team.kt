package com.endeavour.poloaquaticoparedes.model

data class Team (val name: String,
                 val acronym: String,
                 val logo: String,
                 val players: List<Players>,
                 val coaches: List<String>)