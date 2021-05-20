package com.adg.stocksample.presentation.detail

import androidx.lifecycle.*
import com.adg.stocksample.data.StockDataSource
import com.adg.stocksample.data.models.SymbolMonthlyInfoResponse
import com.adg.stocksample.utils.Request
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val stockDataSource: StockDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), DetailScreenActions {

    private val _state: MutableStateFlow<DetailState> =
        MutableStateFlow(DetailState(savedStateHandle.get<String>(SYMBOL)!!))
    val state: StateFlow<DetailState> = _state

    init {
        viewModelScope.launch {
            val search = stockDataSource.getMonthly(state.value.symbol)
            search.fold(
                {
                    _state.value = with(state.value) { copy(results = Request.Error(it)) }
                },
                {
                    _state.value = with(state.value) { copy(results = Request.Success(it)) }
                }
            )
        }
    }

    companion object {
        const val SYMBOL = "symbol"
    }

}

data class DetailState(
    val symbol: String,
    val results: Request<SymbolMonthlyInfoResponse> = Request.Uninitialized
)
