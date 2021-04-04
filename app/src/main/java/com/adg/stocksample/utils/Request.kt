package com.adg.stocksample.utils

sealed class Request<out T> {
    object Uninitialized: Request<Nothing>()
    object Loading: Request<Nothing>()
    data class Error(val throwable: Throwable): Request<Nothing>()
    data class Success<out T>(val result: T): Request<T>()
}