package com.starbase.bankwallet.modules.transactionInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.providers.Translator
import io.horizontalsystems.bankwallet.entities.transactionrecords.bitcoin.TransactionLockState
import io.horizontalsystems.bankwallet.modules.send.SendModule
import io.horizontalsystems.bankwallet.modules.transactions.TransactionType
import io.horizontalsystems.bankwallet.modules.transactions.TransactionViewItem
import io.horizontalsystems.core.helpers.DateHelper
import java.util.*

object TransactionInfoModule {

    class Factory(private val transactionViewItem: TransactionViewItem) :
        ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val adapter =
                com.starbase.bankwallet.core.App.adapterManager.getTransactionsAdapterForWallet(transactionViewItem.wallet)!!
            val service = TransactionInfoService(
                adapter,
                com.starbase.bankwallet.core.App.xRateManager,
                com.starbase.bankwallet.core.App.currencyManager,
                com.starbase.bankwallet.core.App.buildConfigProvider,
                com.starbase.bankwallet.core.App.accountSettingManager
            )
            val factory = TransactionInfoViewItemFactory(
                com.starbase.bankwallet.core.App.numberFormatter,
                Translator,
                DateHelper,
                TransactionInfoAddressMapper
            )
            return TransactionInfoViewModel(
                service,
                factory,
                transactionViewItem.record,
                transactionViewItem.wallet,
                listOf(service)
            ) as T
        }

    }

    data class TitleViewItem(
        val date: Date?,
        val primaryAmountInfo: SendModule.AmountInfo,
        val secondaryAmountInfo: SendModule.AmountInfo?,
        val type: TransactionType,
        val lockState: TransactionLockState?
    )

    data class ExplorerData(val title: String, val url: String?)
}

sealed class TransactionStatusViewItem {
    class Pending(val name: String) : TransactionStatusViewItem()

    //progress in 0.0 .. 1.0
    class Processing(val progress: Double, val name: String) : TransactionStatusViewItem()
    class Completed(val name: String) : TransactionStatusViewItem()
    object Failed : TransactionStatusViewItem()
}
