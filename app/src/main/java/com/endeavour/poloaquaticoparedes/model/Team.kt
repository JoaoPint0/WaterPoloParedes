package com.endeavour.poloaquaticoparedes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class Team (@PrimaryKey val name: String,
                 val acronym: String,
                 val logo: String)
