package com.starbase.bankwallet.modules.createaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.R
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.managers.PassphraseValidator
import io.horizontalsystems.bankwallet.core.providers.Translator

object CreateAccountModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val service = CreateAccountService(
                    com.starbase.bankwallet.core.App.accountFactory,
                    com.starbase.bankwallet.core.App.wordsManager,
                    com.starbase.bankwallet.core.App.accountManager,
                    com.starbase.bankwallet.core.App.walletManager,
                    PassphraseValidator(),
                    com.starbase.bankwallet.core.App.coinKit
            )

            return CreateAccountViewModel(service, listOf(service)) as T
        }
    }

    enum class Kind(val title: String) {
        Mnemonic12(Translator.getString(R.string.CreateWallet_N_Words, 12)),
        Mnemonic24(Translator.getString(R.string.CreateWallet_N_Words, 24)),
    }
}
