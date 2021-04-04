package com.adg.stocksample.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import com.adg.stocksample.R
import com.adg.stocksample.databinding.ActivityStockSampleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockSampleActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_sample)
    }
}