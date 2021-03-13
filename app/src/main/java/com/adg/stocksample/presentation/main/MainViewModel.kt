package com.adg.stocksample.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adg.stocksample.data.SearchEntryResponse
import com.adg.stocksample.data.StockService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stockService: StockService
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
        viewModelScope.launch {
            val search = stockService.search(state.value!!.searchValue)
            _state.postValue(
                with(state.value!!) { copy(searchResults = search.bestMatches) }
            )
        }
    }
}

data class MainState(
    val searchOpen: Boolean = false,
    val searchValue: String = "",
    val searchResults: List<SearchEntryResponse> = emptyList()
)