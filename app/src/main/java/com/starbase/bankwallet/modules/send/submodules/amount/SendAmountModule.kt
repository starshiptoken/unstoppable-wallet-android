package com.starbase.bankwallet.modules.send.submodules.amount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.entities.CoinValue
import io.horizontalsystems.bankwallet.entities.CurrencyValue
import io.horizontalsystems.bankwallet.entities.Wallet
import io.horizontalsystems.bankwallet.modules.send.SendModule
import io.horizontalsystems.bankwallet.modules.send.SendModule.AmountInfo
import io.horizontalsystems.bankwallet.ui.extensions.AmountInputView
import java.math.BigDecimal
import kotlin.math.min

object SendAmountModule {

    interface IView {

        fun setLoading(loading: Boolean)
        fun setAmount(amount: String)
        fun setAvailableBalance(availableBalance: String)
        fun setHint(hint: String)
        fun setValidationError(error: ValidationError?)
        fun revertAmount(amount: String)
        fun setMaxButtonVisible(visible: Boolean)
        fun setInputFields(inputParams: AmountInputView.InputParams)
    }

    interface IViewDelegate {
        fun onViewDidLoad()
        fun onAmountChange(amountString: String)
        fun onSwitchClick()
        fun onMaxClick()
    }

    interface IInteractor {
        var defaultInputType: SendModule.InputType
        fun getRate(): BigDecimal?
        fun onCleared()
    }

    interface IInteractorDelegate {
        fun didUpdateRate(rate: BigDecimal)
        fun willEnterForeground()
    }

    interface IAmountModule {

        val xRate: BigDecimal?
        val sendAmountInfo: SendAmountInfo
        val currentAmount: BigDecimal
        val inputType: SendModule.InputType
        val coinAmount: CoinValue
        val fiatAmount: CurrencyValue?

        @Throws
        fun primaryAmountInfo(): AmountInfo

        @Throws
        fun secondaryAmountInfo(): AmountInfo?

        @Throws
        fun validAmount(): BigDecimal

        fun setLoading(loading: Boolean)
        fun setAmount(amount: BigDecimal)
        fun setAvailableBalance(availableBalance: BigDecimal)
        fun setMinimumAmount(minimumAmount: BigDecimal)
        fun setMaximumAmount(maximumAmount: BigDecimal?)
        fun setMinimumRequiredBalance(minimumRequiredBalance: BigDecimal)
    }

    interface IAmountModuleDelegate {
        fun onChangeAmount()
        fun onChangeInputType(inputType: SendModule.InputType)
        fun onRateUpdated(rate: BigDecimal?) {}
    }

    open class ValidationError : Exception() {
        class EmptyValue(val field: String) : ValidationError()
        class InsufficientBalance(val availableBalance: AmountInfo?) : ValidationError()
        class NotEnoughForMinimumRequiredBalance(val minimumRequiredBalance: CoinValue) : ValidationError()
        class TooFewAmount(val minimumAmount: AmountInfo?) : ValidationError()
        class MaxAmountLimit(val maximumAmount: AmountInfo?) : ValidationError()
    }

    class Factory(private val wallet: Wallet,
                  private val sendHandler: SendModule.ISendHandler) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val view = SendAmountView()
            val coinDecimal = min(wallet.coin.decimal, com.starbase.bankwallet.core.App.appConfigProvider.maxDecimal)
            val currencyDecimal = com.starbase.bankwallet.core.App.appConfigProvider.fiatDecimal
            val baseCurrency = com.starbase.bankwallet.core.App.currencyManager.baseCurrency

            val interactor = SendAmountInteractor(baseCurrency, com.starbase.bankwallet.core.App.xRateManager, com.starbase.bankwallet.core.App.localStorage, wallet.coin, com.starbase.bankwallet.core.App.backgroundManager)
            val sendAmountPresenterHelper =
                    SendAmountPresenterHelper(
                        com.starbase.bankwallet.core.App.numberFormatter, wallet.coin, baseCurrency, coinDecimal,
                            currencyDecimal)
            val presenter = SendAmountPresenter(view, interactor, sendAmountPresenterHelper, wallet.coin, baseCurrency)

            sendHandler.amountModule = presenter
            interactor.delegate = presenter

            return presenter as T
        }
    }

}

sealed class SendAmountInfo {
    object Max : SendAmountInfo()
    class Entered(val amount: BigDecimal) : SendAmountInfo()
    object NotEntered : SendAmountInfo()
}
