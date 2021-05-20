package com.adg.stocksample.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment: Fragment() {

    @Inject
    lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

    private val symbol: String
        get() = requireArguments().getString(SYMBOL)!!

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModel.provideFactory(detailViewModelFactory, symbol)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContent {
            StockSampleTheme {
                DetailScreen(viewModel.state, viewModel)
            }
        }
    }

    private companion object {
        const val SYMBOL = "symbol"
    }
}
