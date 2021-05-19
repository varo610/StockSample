package com.adg.stocksample.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adg.stocksample.data.SearchEntryResponse
import com.adg.stocksample.data.StockDataSource
import com.adg.stocksample.utils.Request
import com.adg.stocksample.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stockDataSource: StockDataSource
) : ViewModel(), MainScreenActions {
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    val navigateToDetail = SingleLiveEvent<String>()

    override fun openSearch() = updateSearch(true)

    override fun hideSearch() = updateSearch(false)

    private fun updateSearch(open: Boolean) {
        _state.value = with(state.value) { copy(searchOpen = open) }
    }

    override fun onSearchValueChange(content: String) {
        _state.value = with(state.value) { copy(searchValue = content) }
    }

    override fun onSearchTriggered() {
        if (_state.value.searchNotEmpty) {
            _state.value = with(state.value) { copy(searchResults = Request.Loading) }
            viewModelScope.launch {
                val search = stockDataSource.search(state.value.searchValue)
                search.fold(
                    {
                        _state.value = with(state.value) {
                            copy(searchResults = Request.Error(it))
                        }
                    },
                    {
                        _state.value = with(state.value) {
                            copy(searchResults = Request.Success(it.bestMatches))
                        }
                    }
                )
            }
        }
    }


    override fun onCellClicked(symbol: String) {
        navigateToDetail.value = symbol
    }
}

data class MainState(
    val searchOpen: Boolean = false,
    val searchValue: String = "",
    val searchResults: Request<List<SearchEntryResponse>> = Request.Uninitialized
) {
    val searchNotEmpty: Boolean
        get() = searchValue.isNullOrEmpty().not()
}