package com.starbase.bankwallet.modules.transactionInfo

import io.horizontalsystems.bankwallet.R
import java.util.*

sealed class TransactionInfoItemType {
    class TransactionType(val leftValue: String, val rightValue: String?) :
        TransactionInfoItemType()

    class Amount(val leftValue: String, val rightValue: ColoredValue) : TransactionInfoItemType()
    class Value(val title: String, val value: String) : TransactionInfoItemType()
    class Decorated(
        val title: String,
        val value: String,
        val actionButton: TransactionInfoActionButton? = null
    ) :
        TransactionInfoItemType()

    class Button(val title: String, val leftIcon: Int, val type: TransactionInfoButtonType) :
        TransactionInfoItemType()

    class Status(val title: String, val leftIcon: Int, val status: TransactionStatusViewItem) :
        TransactionInfoItemType()

    class RawTransaction(val title: String, val actionButton: TransactionInfoActionButton? = null) :
        TransactionInfoItemType()

    class LockState(
        val title: String,
        val leftIcon: Int,
        val date: Date,
        val showLockInfo: Boolean
    ) : TransactionInfoItemType()

    class DoubleSpend(
        val title: String,
        val leftIcon: Int,
        val transactionHash: String,
        val conflictingHash: String
    ) : TransactionInfoItemType()
}

sealed class TransactionInfoButtonType {
    class OpenExplorer(val url: String?) : TransactionInfoButtonType()
    object RevokeApproval : TransactionInfoButtonType()
    object Resend : TransactionInfoButtonType()
}

sealed class TransactionInfoActionButton {
    class ShareButton(val value: String) : TransactionInfoActionButton()
    object CopyButton : TransactionInfoActionButton()

    fun getIcon(): Int {
        return when (this) {
            is ShareButton -> R.drawable.ic_share_20
            CopyButton -> R.drawable.ic_copy_20
        }
    }
}

class ColoredValue(val value: String, val color: Int)
