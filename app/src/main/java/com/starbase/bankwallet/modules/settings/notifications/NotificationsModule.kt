package com.starbase.bankwallet.modules.settings.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App

object NotificationsModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = NotificationsViewModel(com.starbase.bankwallet.core.App.priceAlertManager, com.starbase.bankwallet.core.App.walletManager, com.starbase.bankwallet.core.App.notificationManager, com.starbase.bankwallet.core.App.localStorage)
            return viewModel as T
        }
    }
}
