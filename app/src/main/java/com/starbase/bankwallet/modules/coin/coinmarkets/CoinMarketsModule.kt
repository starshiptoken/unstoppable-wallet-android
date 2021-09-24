package com.starbase.bankwallet.modules.coin.coinmarkets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.coinkit.models.CoinType

object CoinMarketsModule {
    class Factory(private val coinCode: String, private val coinType: CoinType) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CoinMarketsViewModel(coinCode, coinType, com.starbase.bankwallet.core.App.currencyManager, com.starbase.bankwallet.core.App.xRateManager, com.starbase.bankwallet.core.App.numberFormatter) as T
        }
    }
}
