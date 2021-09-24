package com.starbase.bankwallet.modules.market.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.list.MarketListService
import io.horizontalsystems.bankwallet.modules.market.list.MarketListViewModel

object MarketDiscoveryModule {

    class Factory : ViewModelProvider.Factory {
        val service by lazy { MarketDiscoveryService(com.starbase.bankwallet.core.App.xRateManager, com.starbase.bankwallet.core.App.backgroundManager) }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = when (modelClass) {
            MarketDiscoveryViewModel::class.java -> {
                MarketDiscoveryViewModel(service, listOf(service)) as T
            }
            MarketListViewModel::class.java -> {
                val listService = MarketListService(service, com.starbase.bankwallet.core.App.currencyManager)
                MarketListViewModel(listService, com.starbase.bankwallet.core.App.connectivityManager, listOf(listService)) as T
            }
            else -> throw IllegalArgumentException()
        }
    }
}
