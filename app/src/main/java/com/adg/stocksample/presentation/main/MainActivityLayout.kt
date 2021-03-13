package com.adg.stocksample.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme

interface MainActivityActions {
    fun onSearchClick()
    fun onSearchValueChange(content: String)
}

@Composable
fun MainActivityLayout(state: MainState?, mainActivityActions: MainActivityActions) {
    if (state == null) {
        //TODO Error screen
    } else {
        MainScreen(state = state, mainActivityActions = mainActivityActions)
    }
}

@Composable
fun MainScreen(state: MainState, mainActivityActions: MainActivityActions) {
    Scaffold(topBar = {
        MainTopBar(
            searchOpened = state.searchOpen,
            searchValue = state.searchValue,
            onSearchClick = { mainActivityActions.onSearchClick() },
            onSearchValueChange = { value -> mainActivityActions.onSearchValueChange(value) }
        )
    }) {

    }
}


@Composable
fun MainTopBar(
    searchOpened: Boolean,
    searchValue: String,
    onSearchClick: () -> Unit,
    onSearchValueChange: (String) -> Unit
) = TopAppBar(
    navigationIcon = {
        if (searchOpened) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onSearchClick() }
            )
        } else {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = "Trending up",
                modifier = Modifier.padding(8.dp)
            )
        }
    },
    title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (searchOpened) {
                TextField(
                    value = searchValue,
                    onValueChange = onSearchValueChange,
                    placeholder = { Text(text = "Search symbol") },
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colors.primary)
                )
            } else {
                Text(text = "StockSample", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.clickable(onClick = onSearchClick)
                )
            }
        }
    },
    elevation = 8.dp
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StockSampleTheme {
        MainTopBar(
            searchOpened = true,
            searchValue = "",
            onSearchClick = {},
            onSearchValueChange = {})
    }
}