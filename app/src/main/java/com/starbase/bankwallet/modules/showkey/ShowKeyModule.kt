package com.starbase.bankwallet.modules.showkey

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import io.horizontalsystems.bankwallet.R
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.entities.Account
import io.horizontalsystems.core.findNavController
import kotlinx.android.parcel.Parcelize

object ShowKeyModule {
    const val ACCOUNT = "account"

    class Factory(private val account: Account) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = ShowKeyService(account, com.starbase.bankwallet.core.App.pinComponent, com.starbase.bankwallet.core.App.ethereumKitManager, com.starbase.bankwallet.core.App.accountSettingManager)
            return ShowKeyViewModel(service) as T
        }
    }

    fun start(fragment: Fragment, navigateTo: Int, navOptions: NavOptions, account: Account) {
        fragment.findNavController().navigate(navigateTo, bundleOf(ACCOUNT to account), navOptions)
    }

    enum class ShowKeyTab(@StringRes val title: Int) {
        MnemonicPhrase(R.string.ShowKey_TabMnemonicPhrase),
        PrivateKey(R.string.ShowKey_TabPrivateKey)
    }

    @Parcelize
    data class PrivateKey(val blockchain: String, val value: String) : Parcelable

}
