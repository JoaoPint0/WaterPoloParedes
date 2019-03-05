package com.endeavour.poloaquaticoparedes.model

data class CreatePayment(val cardId: Long,
                         val year: Int,
                         val monthPayments: List<Payment>)