package com.starbase.bankwallet.modules.market.advancedsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.list.IMarketListFetcher
import io.horizontalsystems.bankwallet.modules.market.list.MarketListService
import io.horizontalsystems.bankwallet.modules.market.list.MarketListViewModel

object MarketAdvancedSearchResultsModule {
    class Factory(val service: IMarketListFetcher) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val listService = MarketListService(service, com.starbase.bankwallet.core.App.currencyManager)
            return MarketListViewModel(listService, com.starbase.bankwallet.core.App.connectivityManager, listOf(listService)) as T
        }

    }
}
