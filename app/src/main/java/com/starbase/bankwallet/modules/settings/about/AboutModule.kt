package com.starbase.bankwallet.modules.settings.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.ui.helpers.TextHelper

object AboutModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AboutViewModel(com.starbase.bankwallet.core.App.appConfigProvider, TextHelper, com.starbase.bankwallet.core.App.termsManager, com.starbase.bankwallet.core.App.systemInfoManager) as T
        }
    }
}
