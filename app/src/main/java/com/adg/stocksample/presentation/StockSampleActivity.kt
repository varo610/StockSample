package com.adg.stocksample.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.adg.stocksample.presentation.detail.DetailScreen
import com.adg.stocksample.presentation.detail.DetailViewModel
import com.adg.stocksample.presentation.detail.DetailViewModel.Companion.SYMBOL
import com.adg.stocksample.presentation.main.MainScreen
import com.adg.stocksample.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") { backStackEntry ->
                    val mainViewModel: MainViewModel = hiltViewModel(backStackEntry)
                    MainScreen(
                        state = mainViewModel.state,
                        mainScreenActions = mainViewModel
                    ) { symbol ->
                        navController.navigate("detail/$symbol")
                    }
                }
                composable(
                    route = "detail/{symbol}",
                    arguments = listOf(navArgument(SYMBOL) { type = NavType.StringType })
                ) { backStackEntry ->
                    val detailViewModel: DetailViewModel = hiltViewModel(backStackEntry)
                    DetailScreen(detailViewModel.state, detailViewModel)
                }
            }
        }
    }
}