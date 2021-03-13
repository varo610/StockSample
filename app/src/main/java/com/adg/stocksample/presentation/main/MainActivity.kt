package com.adg.stocksample.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), MainActivityActions {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockSampleTheme {
                val state by viewModel.state.observeAsState()
                MainActivityLayout(
                    state = state,
                    mainActivityActions = this
                )
            }
        }
    }

    override fun onSearchClick() = viewModel.onSearchButtonClick()

    override fun onSearchValueChange(content: String) = viewModel.onSearchValueChange(content)
}
