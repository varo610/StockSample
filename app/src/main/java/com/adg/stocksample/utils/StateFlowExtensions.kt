package com.adg.stocksample.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KProperty1

@Composable
fun <T, P> StateFlow<T>.collectPropertyAsState(property: KProperty1<T, P>): State<P> =
    this.map { property.get(it) }.collectAsState(initial = property.get(this.value))