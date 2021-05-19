package com.adg.stocksample.presentation.main

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.animatedVectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adg.stocksample.data.SearchEntryResponse
import com.adg.stocksample.presentation.ui.StockSampleError
import com.adg.stocksample.presentation.ui.StockSampleLoading
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme
import com.adg.stocksample.utils.Request
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlin.math.abs

interface MainScreenActions {
    fun openSearch()
    fun hideSearch()
    fun onSearchValueChange(content: String)
    fun onSearchTriggered()
    fun onCellClicked(symbol: String)
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    MainStateScreen(state = viewModel.state, mainScreenActions = viewModel)
}

@Composable
fun MainStateScreen(state: StateFlow<MainState>, mainScreenActions: MainScreenActions) {
    val searchOpen by state.map { it.searchOpen }.collectAsState(state.value.searchOpen)
    val searchValue by state.map { it.searchValue }.collectAsState(state.value.searchValue)
    Scaffold(topBar = {
        MainTopBar(
            searchOpened = searchOpen,
            searchValue = searchValue,
            openSearch = { mainScreenActions.openSearch() },
            hideSearch = { mainScreenActions.hideSearch() },
            onSearchValueChange = { value -> mainScreenActions.onSearchValueChange(value) },
            onSearchTriggered = { mainScreenActions.onSearchTriggered() }
        )
    }) {
        MainContent(state) { symbol -> mainScreenActions.onCellClicked(symbol) }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainTopBar(
    searchOpened: Boolean,
    searchValue: String,
    openSearch: () -> Unit,
    hideSearch: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    onSearchTriggered: () -> Unit,
) {
    val animationDuration = 500
    val offset by animateDpAsState(
        targetValue = if (searchOpened) 0.dp else 500.dp,
        animationSpec = tween(durationMillis = animationDuration)
    )

    val alpha by animateFloatAsState(
        targetValue = if (searchOpened) 0f else 1f,
        animationSpec = tween(durationMillis = animationDuration)
    )

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
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        },
        title = {
            Surface(Modifier.fillMaxWidth()) {
                SearchView(
                    searchValue,
                    onSearchValueChange,
                    onSearchTriggered,
                    Modifier
                        .fillMaxWidth()
                        .offset(x = offset)
                        .alpha(abs(1 - alpha))
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(alpha)
                ) {
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
        backgroundColor = MaterialTheme.colors.background
    )
}

@OptIn(ExperimentalComposeUiApi::class)
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
        shape = MaterialTheme.shapes.large,
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
fun MainContent(state: StateFlow<MainState>, onCellClicked: (String) -> Unit) {
    val searchResults by state.map { it.searchResults }.collectAsState(state.value.searchResults)
    when (val results = searchResults) {
        Request.Uninitialized -> MainScreenEmpty()
        is Request.Error -> StockSampleError(errorText = results.throwable.message ?: "Error")
        Request.Loading -> StockSampleLoading()
        is Request.Success -> MainScreenSuccess(results.result, onCellClicked)
    }
}

@Composable
fun StockCell(
    searchEntryResponse: SearchEntryResponse,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
) {
    Column(modifier = modifier
        .background(MaterialTheme.colors.background)
        .fillMaxWidth()
        .clickable { onClick(searchEntryResponse.symbol) }
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = searchEntryResponse.symbol,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = searchEntryResponse.name,
                style = MaterialTheme.typography.caption
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

@Composable
fun MainScreenEmpty() = Column(
    verticalArrangement = Arrangement.Center,
    modifier = Modifier.fillMaxSize()
) {
    Text(
        text = "Search in order to see results",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MainScreenSuccess(
    results: List<SearchEntryResponse>,
    onCellClicked: (String) -> Unit
) = LazyColumn {
    items(results) {
        StockCell(
            searchEntryResponse = it,
            onClick = { symbol -> onCellClicked(symbol) }
        )
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
        ),
        onClick = {}
    )
}
