package com.endeavour.poloaquaticoparedes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athletes")
data class Athlete(@ColumnInfo(name = "name") val name: String,
                   @ColumnInfo(name = "gender") val gender: String,
                   @ColumnInfo(name = "mobileNumber") val mobileNumber: Long,
                   @ColumnInfo(name = "email") val email: String,
                   @PrimaryKey val cardId: Long,
                   @ColumnInfo(name = "level") val level: Leagues,
                   @ColumnInfo(name = "enrolled") val enrolled: Boolean)