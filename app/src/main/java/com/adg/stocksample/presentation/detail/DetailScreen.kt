package com.adg.stocksample.presentation.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adg.stocksample.data.models.SymbolMonthlyInfoResponse
import com.adg.stocksample.presentation.ui.StockSampleError
import com.adg.stocksample.presentation.ui.StockSampleLoading
import com.adg.stocksample.utils.Request

interface DetailScreenActions {
}

@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val state by viewModel.state.collectAsState()
    DetailStateScreen(state = state, detailScreenActions = viewModel)
}

@Composable
fun DetailStateScreen(state: DetailState, detailScreenActions: DetailScreenActions) {
    Scaffold(topBar = { DetailTopBar(symbol = state.symbol) }) {
        when (val results = state.results) {
            Request.Uninitialized,
            Request.Loading -> StockSampleLoading()
            is Request.Error -> StockSampleError(errorText = results.throwable.message ?: "Error")
            is Request.Success -> DetailScreenSuccess(results.result)
        }
    }
}

@Composable
fun DetailTopBar(symbol: String) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = "Trending up",
                modifier = Modifier.padding(8.dp)
            )
        },
        title = {
            Text(text = symbol)
        },
        elevation = 8.dp,
    )
}

@Composable
fun DetailScreenSuccess(
    info: SymbolMonthlyInfoResponse
) = LazyColumn {
    items(info.info.entries.toList()){
        Text(text = "${it.key} - ${it.value.close}")
    }
}

