package com.adg.stocksample.data

import com.adg.stocksample.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {
    @GET("query?")
    suspend fun search(
        @Query("keywords") c: String,
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("apikey") apikey: String = BuildConfig.API_KEY
    ): SearchResponse
}