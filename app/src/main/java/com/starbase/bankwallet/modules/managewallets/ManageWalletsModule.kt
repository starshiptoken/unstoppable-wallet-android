package com.starbase.bankwallet.modules.managewallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.blockchainsettings.CoinSettingsViewModel
import io.horizontalsystems.bankwallet.modules.restore.restoreselectcoins.CoinSettingsService
import io.horizontalsystems.bankwallet.modules.restore.restoreselectcoins.RestoreSettingsService
import io.horizontalsystems.bankwallet.modules.restore.restoreselectcoins.RestoreSettingsViewModel

object ManageWalletsModule {

    class Factory : ViewModelProvider.Factory {

        private val restoreSettingsService by lazy {
            RestoreSettingsService(com.starbase.bankwallet.core.App.restoreSettingsManager)
        }

        private val coinSettingsService by lazy {
            CoinSettingsService()
        }

        private val manageWalletsService by lazy {
            ManageWalletsService(com.starbase.bankwallet.core.App.coinManager, com.starbase.bankwallet.core.App.walletManager, com.starbase.bankwallet.core.App.accountManager, restoreSettingsService, coinSettingsService)
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
                ManageWalletsViewModel::class.java -> {
                    ManageWalletsViewModel(manageWalletsService, listOf(manageWalletsService)) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}
