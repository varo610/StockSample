package com.adg.stocksample.data

import com.adg.stocksample.BuildConfig
import com.adg.stocksample.data.models.SymbolMonthlyInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {
    @GET("query?")
    suspend fun search(
        @Query("keywords") keywords: String,
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("apikey") apikey: String = BuildConfig.API_KEY
    ): SearchResponse

    @GET("query?")
    suspend fun getMonthly(
        @Query("symbol") symbol: String,
        @Query("function") function: String = "TIME_SERIES_MONTHLY_ADJUSTED",
        @Query("apikey") apikey: String = BuildConfig.API_KEY
    ): SymbolMonthlyInfoResponse
}