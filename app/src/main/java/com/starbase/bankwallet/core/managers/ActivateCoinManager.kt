package com.starbase.bankwallet.core.managers

import io.horizontalsystems.bankwallet.core.IAccountManager
import io.horizontalsystems.bankwallet.core.IWalletManager
import io.horizontalsystems.bankwallet.entities.Wallet
import io.horizontalsystems.coinkit.CoinKit
import io.horizontalsystems.coinkit.models.CoinType

class ActivateCoinManager(
        private val coinKit: CoinKit,
        private val walletManager: IWalletManager,
        private val accountManager: IAccountManager
) {

    fun activate(coinType: CoinType) {
        val coin = coinKit.getCoin(coinType) ?: return // coin type is not supported

        if (walletManager.activeWallets.any { it.coin == coin })  return // wallet already exists

        val account = accountManager.activeAccount ?: return // active account does not exist

        val wallet = Wallet(coin, account)
        walletManager.save(listOf(wallet))
    }

}
