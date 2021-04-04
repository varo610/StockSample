package com.adg.stocksample.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

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
                MainScreen(viewModel = viewModel)
            }
        }
        viewModel.navigateToDetail.observe(requireActivity()){ symbol ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailFragment(symbol)
            )
        }
    }
}
