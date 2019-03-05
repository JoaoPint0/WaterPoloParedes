package com.endeavour.poloaquaticoparedes.model

import java.util.*

data class ComplexAthlete(val name: String,
                          val address: String,
                          val postalCode: String,
                          val birthday: Date,
                          val gender: String,
                          val mobileNumber: Long,
                          val parents: List<Parent>,
                          val yearsPaid: List<Int>,
                          val email: String,
                          val cardId: Long,
                          val size: String,
                          val level: Leagues,
                          val observations: String,
                          val cc: Boolean,
                          val exam: Boolean,
                          val enrolled: Boolean)