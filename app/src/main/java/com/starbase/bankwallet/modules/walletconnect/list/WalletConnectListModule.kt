package com.starbase.bankwallet.modules.walletconnect.list

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import com.starbase.bankwallet.core.App
import io.horizontalsystems.core.findNavController

object WalletConnectListModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = WalletConnectListService(com.starbase.bankwallet.core.App.walletConnectSessionManager)

            return WalletConnectListViewModel(service) as T
        }
    }

    fun start(fragment: Fragment, navigateTo: Int, navOptions: NavOptions) {
        fragment.findNavController().navigate(navigateTo, null, navOptions)
    }
}
