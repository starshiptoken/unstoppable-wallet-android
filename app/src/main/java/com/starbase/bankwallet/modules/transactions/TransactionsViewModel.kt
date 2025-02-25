package com.starbase.bankwallet.modules.transactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.bankwallet.entities.Wallet
import io.horizontalsystems.core.SingleLiveEvent

class TransactionsViewModel : ViewModel(), TransactionsModule.IView {

    lateinit var delegate: TransactionsModule.IViewDelegate

    val filterItems = MutableLiveData<Pair<List<Wallet?>, Wallet?>>()
    val items = MutableLiveData<List<TransactionViewItem>>()
    val reloadTransactions = SingleLiveEvent<Unit>()
    val showSyncing = MutableLiveData<Boolean>()

    override fun showFilters(filters: List<Wallet?>, selectedFilter: Wallet?) {
        filterItems.postValue(Pair(filters, selectedFilter))
    }

    override fun showTransactions(items: List<TransactionViewItem>) {
        this.items.postValue(items)
    }

    override fun reloadTransactions() {
        reloadTransactions.postValue(Unit)
    }

    override fun showNoTransactions() {
        items.postValue(listOf())
    }

    override fun showSyncing() {
        showSyncing.postValue(true)
    }

    override fun hideSyncing() {
        showSyncing.postValue(false)
    }

    override fun onCleared() {
        delegate.onClear()
    }
}
