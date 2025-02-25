package com.starbase.bankwallet.modules.swap

import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.IAppNumberFormatter
import io.horizontalsystems.bankwallet.core.providers.Translator
import io.horizontalsystems.bankwallet.modules.swap.uniswap.UniswapModule
import io.horizontalsystems.bankwallet.modules.swap.uniswap.UniswapTradeService
import io.horizontalsystems.coinkit.models.Coin
import io.horizontalsystems.uniswapkit.models.TradeData
import io.horizontalsystems.uniswapkit.models.TradeOptions
import io.horizontalsystems.uniswapkit.models.TradeType
import java.math.BigDecimal
import java.math.RoundingMode

class SwapViewItemHelper(private val numberFormatter: IAppNumberFormatter) {

    fun price(price: BigDecimal?, coinFrom: Coin?, coinTo: Coin?): String? {
        if (price == null || coinFrom == null || coinTo == null)
            return null

        val inversePrice = if (price.compareTo(BigDecimal.ZERO) == 0)
            BigDecimal.ZERO
        else
            BigDecimal.ONE.divide(price, price.scale(), RoundingMode.HALF_UP)

        return "${coinTo.code} = ${coinAmount(inversePrice, coinFrom)} "
    }

    fun priceImpactViewItem(
            trade: UniswapTradeService.Trade,
            minLevel: UniswapTradeService.PriceImpactLevel = UniswapTradeService.PriceImpactLevel.Normal
    ): UniswapModule.PriceImpactViewItem? {

        val priceImpact = trade.tradeData.priceImpact ?: return null
        val impactLevel = trade.priceImpactLevel ?: return null
        if (impactLevel < minLevel) {
            return null
        }

        return UniswapModule.PriceImpactViewItem(impactLevel, Translator.getString(R.string.Swap_Percent, priceImpact))
    }

    fun guaranteedAmountViewItem(tradeData: TradeData, coinIn: Coin?, coinOut: Coin?): UniswapModule.GuaranteedAmountViewItem? {
        when (tradeData.type) {
            TradeType.ExactIn -> {
                val amount = tradeData.amountOutMin ?: return null
                val coin = coinOut ?: return null

                return UniswapModule.GuaranteedAmountViewItem(Translator.getString(R.string.Swap_MinimumGot), coinAmount(amount, coin))
            }
            TradeType.ExactOut -> {
                val amount = tradeData.amountInMax ?: return null
                val coin = coinIn ?: return null

                return UniswapModule.GuaranteedAmountViewItem(Translator.getString(R.string.Swap_MaximumPaid), coinAmount(amount, coin))
            }
        }
    }

    fun slippage(allowedSlippage: BigDecimal): String? {
        val defaultTradeOptions = TradeOptions()
        return if (allowedSlippage.compareTo(defaultTradeOptions.allowedSlippagePercent) == 0) {
            null
        } else {
            "$allowedSlippage%"
        }
    }

    fun deadline(ttl: Long): String? {
        val defaultTradeOptions = TradeOptions()
        return if (ttl == defaultTradeOptions.ttl) {
            null
        } else {
            Translator.getString(R.string.Duration_Minutes, ttl / 60)
        }
    }

    fun coinAmount(amount: BigDecimal, coin: Coin, maxFraction: Int? = null): String {
        val fraction = maxFraction ?: numberFormatter.getSignificantDecimalCoin(amount)
        return numberFormatter.formatCoin(amount, coin.code, 0, fraction)
    }

}
