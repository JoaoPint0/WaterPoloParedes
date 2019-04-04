package com.endeavour.poloaquaticoparedes.model

data class Team (val name: String,
                 val acronym: String,
                 val logo: String,
                 val players: MutableList<String>,
                 val coaches: MutableList<String>){
    constructor() : this("", "", "", mutableListOf<String>(), mutableListOf<String>())
}