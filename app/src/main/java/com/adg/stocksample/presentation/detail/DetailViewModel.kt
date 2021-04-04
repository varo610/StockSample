package com.adg.stocksample.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class DetailViewModel @AssistedInject constructor(
    @Assisted private val symbol: String
) : ViewModel(), DetailScreenActions {
    private val _state: MutableLiveData<DetailState> = MutableLiveData(DetailState(symbol))
    val state: LiveData<DetailState> = _state

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

data class DetailState(val symbol: String)
