package com.adg.stocksample.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adg.stocksample.data.models.MetaDataResponse
import com.adg.stocksample.data.models.SymbolMonthlyInfoResponse
import com.adg.stocksample.presentation.ui.StockSampleError
import com.adg.stocksample.presentation.ui.StockSampleLoading
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme
import com.adg.stocksample.utils.Request
import com.adg.stocksample.utils.collectPropertyAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface DetailScreenActions {
}

@Composable
fun DetailScreen(state: StateFlow<DetailState>, detailScreenActions: DetailScreenActions) {
    val symbol by state.collectPropertyAsState(DetailState::symbol)
    val results by state.collectPropertyAsState(DetailState::results)
    Scaffold(topBar = { DetailTopBar(symbol = symbol) }) {
        when (val request = results) {
            Request.Uninitialized,
            Request.Loading -> StockSampleLoading()
            is Request.Error -> StockSampleError(errorText = request.throwable.message ?: "Error")
            is Request.Success -> DetailScreenSuccess(request.result)
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
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
fun DetailScreenSuccess(
    info: SymbolMonthlyInfoResponse
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.LightGray,
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                items(info.info.entries.toList()) {
                    Text(text = "${it.key} - ${it.value.close}")
                }
                item {  }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextSurface(
            modifier = Modifier.fillMaxWidth(),
            text = info.metaData.info
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextSurface(
                modifier = Modifier.weight(1F),
                text = info.metaData.lastRefreshed
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextSurface(
                modifier = Modifier.weight(1F),
                text = info.metaData.timeZone
            )
        }
    }
}

@Composable
fun TextSurface(text: String, modifier: Modifier = Modifier){
    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    StockSampleTheme {
        DetailScreen(
            state = MutableStateFlow(
                DetailState(
                    symbol = "APPL",
                    results = Request.Success(SymbolMonthlyInfoResponse(
                        info = mapOf(),
                        metaData = MetaDataResponse(
                            info = "info",
                            symbol = "symbol",
                            lastRefreshed = "lastRefreshed",
                            timeZone = "timezone"
                        )
                    ))
                )
            ),
            detailScreenActions = object : DetailScreenActions {}
        )
    }
}

