package com.rubenlr.demo.data.entities

import java.math.BigDecimal

data class TransactionRate (
    val fromSymbol: String,
    val toSymbol: String,
    val rate: BigDecimal
)