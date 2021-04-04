package com.adg.stocksample.presentation.detail

import androidx.lifecycle.*
import com.adg.stocksample.data.StockDataSource
import com.adg.stocksample.data.models.SymbolMonthlyInfoResponse
import com.adg.stocksample.utils.Request
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    @Assisted private val symbol: String,
    private val stockDataSource: StockDataSource
) : ViewModel(), DetailScreenActions {
    private val _state: MutableLiveData<DetailState> = MutableLiveData(DetailState(symbol))
    val state: LiveData<DetailState> = _state

    init {
        viewModelScope.launch {
            val search = stockDataSource.getMonthly(state.value!!.symbol)
            search.fold(
                {
                    _state.postValue(
                        with(state.value!!) { copy(results = Request.Error(it)) }
                    )
                },
                {
                    _state.postValue(
                        with(state.value!!) {
                            copy(results = Request.Success(it))
                        }
                    )
                }
            )
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(symbol: String): DetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            symbol: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(symbol) as T
            }
        }
    }

}

data class DetailState(
    val symbol: String,
    val results: Request<SymbolMonthlyInfoResponse> = Request.Uninitialized
)
