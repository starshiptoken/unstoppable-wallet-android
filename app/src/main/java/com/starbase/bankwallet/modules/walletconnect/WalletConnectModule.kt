package com.starbase.bankwallet.modules.walletconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App

object WalletConnectModule {

    class Factory(private val remotePeerId: String?) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = WalletConnectService(remotePeerId, com.starbase.bankwallet.core.App.walletConnectManager, com.starbase.bankwallet.core.App.walletConnectSessionManager, com.starbase.bankwallet.core.App.walletConnectRequestManager, com.starbase.bankwallet.core.App.connectivityManager)

            return WalletConnectViewModel(service, listOf(service)) as T
        }
    }

}
