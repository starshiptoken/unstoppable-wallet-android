package com.starbase.bankwallet.modules.send.submodules.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.entities.Address
import io.horizontalsystems.bankwallet.modules.send.SendModule
import io.horizontalsystems.bankwallet.modules.swap.settings.AddressResolutionService
import io.horizontalsystems.bankwallet.modules.swap.settings.RecipientAddressViewModel
import io.horizontalsystems.coinkit.models.Coin
import java.math.BigDecimal

object SendAddressModule {

    interface IView {
        fun setAddress(address: String?)
        fun setAddressError(error: Exception?)
        fun setAddressInputAsEditable(editable: Boolean)
    }

    interface IViewDelegate {
        fun onViewDidLoad()
        fun onAddressDeleteClicked()
    }

    interface IInteractor {
        val addressFromClipboard: String?

        fun parseAddress(address: String): Pair<String, BigDecimal?>
    }

    interface IInteractorDelegate

    interface IAddressModule {
        var currentAddress: Address?

        @Throws
        fun validAddress(): Address
        fun validateAddress()
    }

    interface IAddressModuleDelegate {
        fun validate(address: String)

        fun onUpdateAddress()
        fun onUpdateAmount(amount: BigDecimal)
    }

    open class ValidationError : Exception() {
        class InvalidAddress : ValidationError()
        class EmptyValue : ValidationError()
    }

    class Factory(
            private val coin: Coin,
            private val sendHandler: SendModule.ISendHandler,
            private val addressModuleDelete: IAddressModuleDelegate,
            private val isResolutionEnabled: Boolean = true,
            private val placeholder: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val addressParser = com.starbase.bankwallet.core.App.addressParserFactory.parser(coin)
            val presenter = SendAddressPresenter(addressModuleDelete)

            val resolutionService = AddressResolutionService(coin.code, isResolutionEnabled)
            val viewModel = RecipientAddressViewModel(presenter, resolutionService, addressParser, placeholder, listOf(resolutionService))

            sendHandler.addressModule = presenter

            return viewModel as T
        }
    }
}
