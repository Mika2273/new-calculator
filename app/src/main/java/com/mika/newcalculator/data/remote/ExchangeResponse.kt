package com.mika.newcalculator.data.remote

data class ExchangeResponse (
    val amount: Float,
    val base: String,
    val date: String,
    val rates: Map<String, Float>
)