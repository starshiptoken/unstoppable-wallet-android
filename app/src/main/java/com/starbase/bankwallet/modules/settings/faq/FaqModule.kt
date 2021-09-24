package com.starbase.bankwallet.modules.settings.faq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.managers.FaqManager

object FaqModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val faqRepository = FaqRepository(FaqManager, com.starbase.bankwallet.core.App.connectivityManager)

            return FaqViewModel(faqRepository, com.starbase.bankwallet.core.App.languageManager) as T
        }
    }
}

sealed class DataState<T> {
    class Loading<T> : DataState<T>()
    class Success<T>(val data: T) : DataState<T>()
    class Error<T>(val throwable: Throwable) : DataState<T>()
}
