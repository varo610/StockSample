package com.adg.stocksample.di

import com.adg.stocksample.data.StockService
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideStockService(
        networkFlipperPlugin: NetworkFlipperPlugin
    ): StockService = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
                .build()
        )
        .baseUrl("https://www.alphavantage.co/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(StockService::class.java)
}