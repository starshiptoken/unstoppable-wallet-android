package com.starbase.bankwallet.modules.market.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.posts.MarketPostService

object MarketOverviewModule {

    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = MarketOverviewService(com.starbase.bankwallet.core.App.xRateManager, com.starbase.bankwallet.core.App.backgroundManager, com.starbase.bankwallet.core.App.currencyManager)
            val postsService = MarketPostService(com.starbase.bankwallet.core.App.xRateManager, com.starbase.bankwallet.core.App.backgroundManager)
            return MarketOverviewViewModel(service, postsService, listOf(service)) as T
        }

    }

    data class PostViewItem(
            val timeAgo: String,
            val imageUrl: String?,
            val source: String,
            val title: String,
            val url: String,
            val body: String
    )

}
