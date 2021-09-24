package com.starbase.bankwallet.modules.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.coinkit.models.CoinType

object CoinModule {

    class Factory(private val coinTitle: String, private val coinType: CoinType, private val coinCode: String) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val currency = com.starbase.bankwallet.core.App.currencyManager.baseCurrency
            val service = CoinService(
                    coinType,
                    currency,
                    com.starbase.bankwallet.core.App.xRateManager,
                    com.starbase.bankwallet.core.App.chartTypeStorage,
                    com.starbase.bankwallet.core.App.priceAlertManager,
                    com.starbase.bankwallet.core.App.notificationManager,
                    com.starbase.bankwallet.core.App.marketFavoritesManager,
                    com.starbase.bankwallet.core.App.appConfigProvider.guidesUrl
            )
            return CoinViewModel(service, coinCode, coinTitle, CoinViewFactory(currency, com.starbase.bankwallet.core.App.numberFormatter), listOf(service)) as T
        }

    }
}
