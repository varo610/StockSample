package com.adg.stocksample.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adg.stocksample.data.SearchEntryResponse
import com.adg.stocksample.data.StockDataSource
import com.adg.stocksample.utils.Either
import com.adg.stocksample.utils.Request
import com.adg.stocksample.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stockDataSource: StockDataSource
) : ViewModel(), MainScreenActions {
    private val _state: MutableLiveData<MainState> = MutableLiveData(MainState())
    val state: LiveData<MainState> = _state

    val navigateToDetail = SingleLiveEvent<String>()

    override fun openSearch() = updateSearch(true)

    override fun hideSearch() = updateSearch(false)

    private fun updateSearch(open: Boolean) {
        _state.value = with(state.value!!) { copy(searchOpen = open) }
    }

    override fun onSearchValueChange(content: String) {
        _state.value = with(state.value!!) { copy(searchValue = content) }
    }

    override fun onSearchTriggered() {
        _state.value = with(state.value!!) { copy(searchResults = Request.Loading) }
        viewModelScope.launch {
            val search = stockDataSource.search(state.value!!.searchValue)
            search.fold(
                {
                    _state.postValue(
                        with(state.value!!) {
                            copy(searchResults = Request.Error(it))
                        }
                    )
                },
                {
                    _state.postValue(
                        with(state.value!!) {
                            copy(searchResults = Request.Success(it.bestMatches))
                        }
                    )
                }
            )
        }
    }

    override fun onCellClicked(symbol: String) {
        navigateToDetail.value = symbol
    }
}

data class MainState(
    val searchOpen: Boolean = false,
    val searchValue: String = "",
    val searchResults: Request<List<SearchEntryResponse>> = Request.Loading
) {
    val isLoading: Boolean
        get() = searchResults is Request.Loading
}