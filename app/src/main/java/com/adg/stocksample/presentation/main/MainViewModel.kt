package com.adg.stocksample.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state: MutableLiveData<MainState> = MutableLiveData(MainState())
    val state: LiveData<MainState> = _state

    fun updateSearch(open: Boolean) {
        _state.value = with(state.value!!) { copy(searchOpen = open) }
    }

    fun onSearchValueChange(content: String) {
        _state.value = with(state.value!!) { copy(searchValue = content) }
    }
}

data class MainState(
    val searchOpen: Boolean = false,
    val searchValue: String = ""
)