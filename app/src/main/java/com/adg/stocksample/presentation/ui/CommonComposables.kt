package com.adg.stocksample.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StockSampleLoading() = Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize(),
) {
    CircularProgressIndicator(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.size(100.dp)
    )
}

@Composable
fun StockSampleError(errorText: String) = Column(
    verticalArrangement = Arrangement.Center,
    modifier = Modifier.fillMaxSize()
) {
    Text(
        text = errorText,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}