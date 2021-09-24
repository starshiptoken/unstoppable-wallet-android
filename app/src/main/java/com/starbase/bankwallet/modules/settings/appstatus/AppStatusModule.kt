package com.starbase.bankwallet.modules.settings.appstatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.ui.helpers.TextHelper

object AppStatusModule {

    interface IView {
        fun setAppStatus(status: Map<String, Any>)
        fun showCopied()
    }

    interface IViewDelegate {
        fun viewDidLoad()
        fun didTapCopy(text: String)
    }

    interface IInteractor {
        val status: Map<String, Any>

        fun copyToClipboard(text: String)
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val view = AppStatusView()
            val appStatusService = AppStatusService(
                    com.starbase.bankwallet.core.App.systemInfoManager,
                    com.starbase.bankwallet.core.App.localStorage,
                    com.starbase.bankwallet.core.App.accountManager,
                    com.starbase.bankwallet.core.App.walletManager,
                    com.starbase.bankwallet.core.App.adapterManager,
                    com.starbase.bankwallet.core.App.ethereumKitManager,
                    com.starbase.bankwallet.core.App.binanceSmartChainKitManager,
                    com.starbase.bankwallet.core.App.binanceKitManager
            )
            val interactor = AppStatusInteractor(appStatusService, TextHelper)
            val presenter = AppStatusPresenter(view, interactor)

            return presenter as T
        }
    }
}
