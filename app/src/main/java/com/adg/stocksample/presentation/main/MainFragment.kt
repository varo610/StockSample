package com.adg.stocksample.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme
import com.adg.stocksample.utils.Request
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        if (savedInstanceState != null && viewModel.state.value.searchResults is Request.Uninitialized) {
            viewModel.onSearchValueChange(savedInstanceState.getString(SEARCH_VALUE) ?: "")
            viewModel.onSearchTriggered()
        }
        setContent {
            StockSampleTheme { MainScreen(viewModel.state, viewModel) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sideEffects
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { sideEffect ->
                when (sideEffect) {
                    is MainSideEffects.NavigateToDetail -> findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToDetailFragment(sideEffect.symbol)
                    )
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.state.value.let {
            outState.putString(SEARCH_VALUE, it.searchValue)
        }
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
    }
}
