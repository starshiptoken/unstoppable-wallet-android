package com.starbase.bankwallet.modules.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.AdapterState
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.BalanceData
import io.horizontalsystems.bankwallet.entities.Wallet
import io.horizontalsystems.xrateskit.entities.LatestRate

object BalanceModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val activeAccountService = ActiveAccountService(com.starbase.bankwallet.core.App.accountManager)

            val balanceService2 = BalanceService2(
                BalanceActiveWalletRepository(com.starbase.bankwallet.core.App.walletManager, com.starbase.bankwallet.core.App.accountSettingManager),
                BalanceXRateRepository(com.starbase.bankwallet.core.App.currencyManager, com.starbase.bankwallet.core.App.xRateManager),
                BalanceAdapterRepository(
                    com.starbase.bankwallet.core.App.adapterManager, BalanceCache(
                        com.starbase.bankwallet.core.App.appDatabase.enabledWalletsCacheDao())),
                NetworkTypeChecker(com.starbase.bankwallet.core.App.accountSettingManager),
                com.starbase.bankwallet.core.App.localStorage,
                com.starbase.bankwallet.core.App.connectivityManager,
                BalanceSorter(),
            )

            val rateAppService = RateAppService(com.starbase.bankwallet.core.App.rateAppManager)

            return BalanceViewModel(
                balanceService2,
                rateAppService,
                activeAccountService,
                BalanceViewItemFactory(),
                com.starbase.bankwallet.core.App.appConfigProvider.reportEmail
            ) as T
        }
    }

    data class BalanceItem(
        val wallet: Wallet,
        val mainNet: Boolean,
        val balanceData: BalanceData,
        val state: AdapterState,
        val latestRate: LatestRate? = null
    ) {
        val fiatValue get() = latestRate?.rate?.let { balanceData.available.times(it) }
    }
}
