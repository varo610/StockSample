package com.adg.stocksample.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    val bestMatches: List<SearchEntryResponse>
)

@JsonClass(generateAdapter = true)
data class SearchEntryResponse(
    @Json(name = "1. symbol")
    val symbol: String,
    @Json(name = "2. name")
    val name: String,
    @Json(name = "3. type")
    val type: String,
    @Json(name = "4. region")
    val region: String,
    @Json(name = "5. marketOpen")
    val marketOpen: String,
    @Json(name = "6. marketClose")
    val marketClose: String,
    @Json(name = "7. timezone")
    val timezone: String,
    @Json(name = "8. currency")
    val currency: String,
    @Json(name = "9. matchScore")
    val matchScore: String,
)