package com.starbase.bankwallet.modules.settings.experimental.bitcoinhodling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App

object BitcoinHodlingModule {

    interface IView {
        fun setLockTime(enabled: Boolean)
    }

    interface IViewDelegate {
        fun onLoad()
        fun onSwitchLockTime(enabled: Boolean)
    }

    interface IInteractor {
        var isLockTimeEnabled: Boolean
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val view = BitcoinHodlingView()
            val interactor = BitcoinHodlingInteractor(com.starbase.bankwallet.core.App.localStorage)
            val presenter = BitcoinHodlingPresenter(view, interactor)

            return presenter as T
        }
    }
}
