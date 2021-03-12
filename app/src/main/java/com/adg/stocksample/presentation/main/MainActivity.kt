package com.adg.stocksample.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockSampleTheme {
                MainActivityLayout()
            }
        }
    }
}

@Composable
fun MainActivityLayout() {
    Scaffold(topBar = { MainTopBar() }) {

    }
}


@Composable
fun MainTopBar() = TopAppBar(title = {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.TrendingUp, "Trending up")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "StockSample")
    }
})

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StockSampleTheme {
        MainActivityLayout()
    }
}