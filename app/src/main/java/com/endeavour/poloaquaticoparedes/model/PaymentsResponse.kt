package com.endeavour.poloaquaticoparedes.model

data class PaymentsResponse(val cardId: Int,
                            val year: Int,
                            val monthPayments: List<Payment>)
