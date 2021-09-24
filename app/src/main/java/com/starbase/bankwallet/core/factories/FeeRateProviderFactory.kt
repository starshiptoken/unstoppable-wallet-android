package com.starbase.bankwallet.core.factories

import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.IFeeRateProvider
import io.horizontalsystems.bankwallet.core.providers.*
import io.horizontalsystems.coinkit.models.Coin
import io.horizontalsystems.coinkit.models.CoinType

object FeeRateProviderFactory {
    private val feeRateProvider = com.starbase.bankwallet.core.App.feeRateProvider

    fun provider(coin: Coin?): IFeeRateProvider? {
        return when (coin?.type) {
            is CoinType.Bitcoin -> BitcoinFeeRateProvider(feeRateProvider)
            is CoinType.Litecoin -> LitecoinFeeRateProvider(feeRateProvider)
            is CoinType.BitcoinCash -> BitcoinCashFeeRateProvider(feeRateProvider)
            is CoinType.Dash -> DashFeeRateProvider(feeRateProvider)
            is CoinType.BinanceSmartChain, is CoinType.Bep20 -> BinanceSmartChainFeeRateProvider(feeRateProvider)
            is CoinType.Ethereum, is CoinType.Erc20 -> EthereumFeeRateProvider(feeRateProvider)
            else -> null
        }
    }

}
