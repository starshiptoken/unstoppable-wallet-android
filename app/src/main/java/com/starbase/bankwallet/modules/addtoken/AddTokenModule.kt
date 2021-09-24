package com.starbase.bankwallet.modules.addtoken

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.coinkit.models.Coin
import kotlinx.android.parcel.Parcelize

object AddTokenModule {
    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val activeAccount = com.starbase.bankwallet.core.App.accountManager.activeAccount!!
            val erc20NetworkType = com.starbase.bankwallet.core.App.accountSettingManager.ethereumNetwork(activeAccount).networkType
            val bep20NetworkType = com.starbase.bankwallet.core.App.accountSettingManager.binanceSmartChainNetwork(activeAccount).networkType

            val ethereumService = AddEvmTokenBlockchainService(com.starbase.bankwallet.core.App.appConfigProvider, com.starbase.bankwallet.core.App.networkManager, erc20NetworkType)
            val binanceSmartChainService = AddEvmTokenBlockchainService(com.starbase.bankwallet.core.App.appConfigProvider, com.starbase.bankwallet.core.App.networkManager, bep20NetworkType)
            val binanceService = AddBep2TokenBlockchainService(com.starbase.bankwallet.core.App.buildConfigProvider)
            val services = listOf(ethereumService, binanceSmartChainService, binanceService)

            val service = AddTokenService(com.starbase.bankwallet.core.App.coinManager, services, com.starbase.bankwallet.core.App.walletManager, com.starbase.bankwallet.core.App.accountManager)
            val viewModel = AddTokenViewModel(service)

            return viewModel as T
        }
    }

    data class ViewItem(val coinType: String?, val coinName: String, val symbol: String, val decimal: Int)

    sealed class State {
        object Idle : State()
        object Loading : State()
        class AlreadyExists(val coin: Coin) : State()
        class Fetched(val coin: Coin) : State()
        class Failed(val error: Throwable) : State()
    }
}

@Parcelize
enum class TokenType(val value: String) : Parcelable {
    Erc20("erc20"),
    Bep20("bep20"),
    Bep2("bep2");
}
