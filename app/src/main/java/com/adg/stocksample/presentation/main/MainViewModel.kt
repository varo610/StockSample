package com.adg.stocksample.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adg.stocksample.data.SearchEntryResponse
import com.adg.stocksample.data.StockDataSource
import com.adg.stocksample.utils.Either
import com.adg.stocksample.utils.Request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stockDataSource: StockDataSource
) : ViewModel() {
    private val _state: MutableLiveData<MainState> = MutableLiveData(MainState())
    val state: LiveData<MainState> = _state

    fun updateSearch(open: Boolean) {
        _state.value = with(state.value!!) { copy(searchOpen = open) }
    }

    fun onSearchValueChange(content: String) {
        _state.value = with(state.value!!) { copy(searchValue = content) }
    }

    fun onSearchTriggered() {
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
}

data class MainState(
    val searchOpen: Boolean = false,
    val searchValue: String = "",
    val searchResults: Request<List<SearchEntryResponse>> = Request.Loading
) {
    val isLoading: Boolean
        get() = searchResults is Request.Loading
}