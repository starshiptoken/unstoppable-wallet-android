package com.starbase.bankwallet.modules.balanceonboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App

object BalanceOnboardingModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BalanceOnboardingViewModel(com.starbase.bankwallet.core.App.accountManager, com.starbase.bankwallet.core.App.walletManager) as T
        }
    }
}
