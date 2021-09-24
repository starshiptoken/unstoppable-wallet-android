package com.starbase.bankwallet.modules.restore.restoreselectcoins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.entities.AccountType
import io.horizontalsystems.bankwallet.modules.blockchainsettings.CoinSettingsViewModel
import io.horizontalsystems.bankwallet.modules.enablecoins.*

object RestoreSelectCoinsModule {

    class Factory(private val accountType: AccountType) : ViewModelProvider.Factory {

        private val enableCoinsService by lazy {
            EnableCoinsService(
                    com.starbase.bankwallet.core.App.buildConfigProvider,
                    com.starbase.bankwallet.core.App.coinManager,
                    EnableCoinsBep2Provider(com.starbase.bankwallet.core.App.buildConfigProvider),
                    EnableCoinsEip20Provider(com.starbase.bankwallet.core.App.networkManager, com.starbase.bankwallet.core.App.appConfigProvider, EnableCoinsEip20Provider.EnableCoinMode.Erc20),
                    EnableCoinsEip20Provider(com.starbase.bankwallet.core.App.networkManager, com.starbase.bankwallet.core.App.appConfigProvider, EnableCoinsEip20Provider.EnableCoinMode.Bep20)
            )
        }

        private val restoreSettingsService by lazy {
            RestoreSettingsService(com.starbase.bankwallet.core.App.restoreSettingsManager)
        }
        private val coinSettingsService by lazy {
            CoinSettingsService()
        }

        private val restoreSelectCoinsService by lazy {
            RestoreSelectCoinsService(
                    accountType,
                    com.starbase.bankwallet.core.App.accountFactory,
                    com.starbase.bankwallet.core.App.accountManager,
                    com.starbase.bankwallet.core.App.walletManager,
                    com.starbase.bankwallet.core.App.coinManager,
                    enableCoinsService,
                    restoreSettingsService,
                    coinSettingsService)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                RestoreSettingsViewModel::class.java -> {
                    RestoreSettingsViewModel(restoreSettingsService, listOf(restoreSettingsService)) as T
                }
                CoinSettingsViewModel::class.java -> {
                    CoinSettingsViewModel(coinSettingsService, listOf(coinSettingsService)) as T
                }
                EnableCoinsViewModel::class.java -> {
                    EnableCoinsViewModel(enableCoinsService) as T
                }
                RestoreSelectCoinsViewModel::class.java -> {
                    RestoreSelectCoinsViewModel(restoreSelectCoinsService, listOf(restoreSelectCoinsService)) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}

