package com.adg.stocksample.presentation.main

import android.graphics.drawable.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme

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
                        .padding(16.dp)
                        .clickable(onClick = hideSearch)
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
                            .padding(16.dp)
                            .clickable(onClick = openSearch)
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
    )
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    SearchView(searchValue = "Aaaaa", onSearchValueChange = { }, onSearchTriggered = {})
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