package com.adg.stocksample.data

import com.adg.stocksample.data.models.SymbolMonthlyInfoResponse
import com.adg.stocksample.utils.Either
import javax.inject.Inject

class StockDataSource @Inject constructor(
    private val stockService: StockService
) {
    suspend fun search(keywords: String): Either<Throwable, SearchResponse> = try {
        Either.Right(stockService.search(keywords))
    } catch (t: Throwable) {
        Either.Left(t)
    }

    suspend fun getMonthly(symbol: String): Either<Throwable, SymbolMonthlyInfoResponse> = try {
        Either.Right(stockService.getMonthly(symbol))
    } catch (t: Throwable) {
        Either.Left(t)
    }
}
