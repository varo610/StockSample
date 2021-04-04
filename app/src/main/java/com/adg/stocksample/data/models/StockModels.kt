package com.adg.stocksample.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SymbolMonthlyInfoResponse(
    @Json(name = "Meta Data")
    val metaData: MetaDataResponse,
    @Json(name = "Monthly Adjusted Time Series")
    val info: Map<String,StockSampleResponse>
)

@JsonClass(generateAdapter = true)
data class MetaDataResponse(
    @Json(name = "1. Information")
    val info: String,
    @Json(name = "2. Symbol")
    val symbol: String,
    @Json(name = "3. Last Refreshed")
    val lastRefreshed: String,
    @Json(name = "4. Time Zone")
    val timeZone: String
)

@JsonClass(generateAdapter = true)
data class StockSampleResponse(
    @Json(name = "4. close")
    val close: Double
)

