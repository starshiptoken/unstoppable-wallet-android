package com.starbase.bankwallet.modules.market.metrics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.entities.CurrencyValue
import io.horizontalsystems.core.entities.Currency
import io.horizontalsystems.xrateskit.entities.GlobalCoinMarket
import java.math.BigDecimal

object MarketMetricsModule {
    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = MarketMetricsService(com.starbase.bankwallet.core.App.xRateManager, com.starbase.bankwallet.core.App.backgroundManager, com.starbase.bankwallet.core.App.currencyManager)
            return MarketMetricsViewModel(service, listOf(service)) as T
        }

    }

}

data class MarketMetricsItem (
        val currencyCode: String,
        val volume24h: CurrencyValue,
        val volume24hDiff24h: BigDecimal,
        val marketCap: CurrencyValue,
        val marketCapDiff24h: BigDecimal,
        var btcDominance: BigDecimal = BigDecimal.ZERO,
        var btcDominanceDiff24h: BigDecimal = BigDecimal.ZERO,
        var defiMarketCap: CurrencyValue,
        var defiMarketCapDiff24h: BigDecimal = BigDecimal.ZERO,
        var defiTvl: CurrencyValue,
        var defiTvlDiff24h: BigDecimal = BigDecimal.ZERO,
        val btcDominancePoints: List<MarketMetricsPoint>,
        val volume24Points: List<MarketMetricsPoint>,
        val defiMarketCapPoints: List<MarketMetricsPoint>,
        val defiTvlPoints: List<MarketMetricsPoint>
){
    companion object{
        fun createFromGlobalCoinMarket(globalCoinMarket: GlobalCoinMarket, currency: Currency): MarketMetricsItem {
            return MarketMetricsItem(
                    globalCoinMarket.currencyCode,
                    CurrencyValue(currency, globalCoinMarket.volume24h),
                    globalCoinMarket.volume24hDiff24h,
                    CurrencyValue(currency, globalCoinMarket.marketCap),
                    globalCoinMarket.marketCapDiff24h,
                    globalCoinMarket.btcDominance,
                    globalCoinMarket.btcDominanceDiff24h,
                    CurrencyValue(currency, globalCoinMarket.defiMarketCap),
                    globalCoinMarket.defiMarketCapDiff24h,
                    CurrencyValue(currency, globalCoinMarket.defiTvl),
                    globalCoinMarket.defiTvlDiff24h,
                    btcDominancePoints = globalCoinMarket.globalCoinMarketPoints.map { MarketMetricsPoint(it.btcDominance, it.timestamp) },
                    volume24Points = globalCoinMarket.globalCoinMarketPoints.map { MarketMetricsPoint(it.volume24h, it.timestamp) },
                    defiMarketCapPoints = globalCoinMarket.globalCoinMarketPoints.map { MarketMetricsPoint(it.defiMarketCap, it.timestamp) },
                    defiTvlPoints = globalCoinMarket.globalCoinMarketPoints.map { MarketMetricsPoint(it.defiTvl, it.timestamp) }
            )
        }
    }
}

data class MarketMetricsPoint(val value: BigDecimal, val timestamp: Long)
