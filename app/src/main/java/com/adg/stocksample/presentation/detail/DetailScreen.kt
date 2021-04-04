package com.adg.stocksample.presentation.detail

import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adg.stocksample.presentation.main.MainViewModel
import com.adg.stocksample.presentation.main.SearchView

interface DetailScreenActions {
}

@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val state by viewModel.state.observeAsState()
    DetailStateScreen(state = state!!, detailScreenActions = viewModel)
}

@Composable
fun DetailStateScreen(state: DetailState, detailScreenActions: DetailScreenActions) {
    Scaffold(topBar = { DetailTopBar(symbol = state.symbol) }) {
        Text(text = "Detail")
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
