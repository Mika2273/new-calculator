package com.mika.newcalculator.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface FrankfurterApi {
    @GET("latest")
    suspend fun getEuroToYenRate(
        @Query("from") from: String = "EUR",
        @Query("to") to: String = "JPY"
    ): ExchangeResponse
}