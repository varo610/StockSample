package com.adg.stocksample.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adg.stocksample.data.SearchEntryResponse
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme
import com.adg.stocksample.utils.Request

interface MainActivityActions {
    fun openSearch()
    fun hideSearch()
    fun onSearchValueChange(content: String)
    fun onSearchTriggered()
}

@ExperimentalComposeUiApi
@Composable
fun MainActivityLayout(state: MainState?, mainActivityActions: MainActivityActions) {
    if (state == null) {
        //TODO Error screen
    } else {
        MainScreen(state = state, mainActivityActions = mainActivityActions)
    }
}

@ExperimentalComposeUiApi
@Composable
fun MainScreen(state: MainState, mainActivityActions: MainActivityActions) {
    Scaffold(topBar = {
        MainTopBar(
            searchOpened = state.searchOpen,
            searchValue = state.searchValue,
            openSearch = { mainActivityActions.openSearch() },
            hideSearch = { mainActivityActions.hideSearch() },
            onSearchValueChange = { value -> mainActivityActions.onSearchValueChange(value) },
            onSearchTriggered = { mainActivityActions.onSearchTriggered() }
        )
    }) {
        when (state.searchResults) {
            is Request.Error -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = state.searchResults.throwable.message ?: "Error",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Request.Loading -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "LOADING",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            is Request.Success -> {
                LazyColumn { items(state.searchResults.result) {
                    StockCell(searchEntryResponse = it) }
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun MainTopBar(
    searchOpened: Boolean,
    searchValue: String,
    openSearch: () -> Unit,
    hideSearch: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    onSearchTriggered: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            if (searchOpened) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable(onClick = hideSearch)
                        .padding(16.dp)
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
                    SearchView(
                        searchValue,
                        onSearchValueChange,
                        onSearchTriggered,
                        Modifier.weight(1f)
                    )
                } else {
                    Text(text = "StockSample", modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .clickable(onClick = openSearch)
                            .padding(16.dp)
                    )
                }
            }
        },
        elevation = 8.dp,
        backgroundColor = if (searchOpened) {
            MaterialTheme.colors.background
        } else {
            MaterialTheme.colors.primary
        }
    )
}

@ExperimentalComposeUiApi
@Composable
fun SearchView(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = searchValue,
        onValueChange = onSearchValueChange,
        placeholder = { Text(text = "Search symbol") },
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.background,
        ),
        textStyle = MaterialTheme.typography.body1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hideSoftwareKeyboard()
                onSearchTriggered()
            }
        ),
        modifier = modifier
            .focusRequester(focusRequester)
    )
    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
}

@Composable
fun StockCell(
    searchEntryResponse: SearchEntryResponse,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
) {
    Column(modifier = modifier
        .background(MaterialTheme.colors.background)
        .fillMaxWidth()
        .clickable { }
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = searchEntryResponse.symbol,
                fontSize = MaterialTheme.typography.body1.fontSize
            )
            Text(
                text = searchEntryResponse.name,
                fontSize = MaterialTheme.typography.caption.fontSize
            )
        }
        if (showDivider) {
            Surface(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {}
        }
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StockSampleTheme {
        MainTopBar(
            searchOpened = true,
            searchValue = "",
            openSearch = {},
            hideSearch = {},
            onSearchValueChange = {},
            onSearchTriggered = {},
        )
    }
}

@Preview
@Composable
fun CellPreview() {
    StockCell(
        searchEntryResponse = SearchEntryResponse(
            symbol = "AAPL",
            name = "Apple Inc",
            type = "",
            region = "",
            marketOpen = "",
            marketClose = "",
            timezone = "",
            currency = "",
            matchScore = "",
        )
    )
}
