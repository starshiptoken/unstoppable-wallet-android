package com.starbase.bankwallet.modules.market.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.list.MarketListService
import io.horizontalsystems.bankwallet.modules.market.list.MarketListViewModel

object MarketFavoritesModule {

    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = MarketFavoritesService(com.starbase.bankwallet.core.App.xRateManager, com.starbase.bankwallet.core.App.marketFavoritesManager, com.starbase.bankwallet.core.App.backgroundManager)
            val listService = MarketListService(service, com.starbase.bankwallet.core.App.currencyManager)
            return MarketListViewModel(listService, com.starbase.bankwallet.core.App.connectivityManager, listOf(listService, service)) as T
        }

    }

}

