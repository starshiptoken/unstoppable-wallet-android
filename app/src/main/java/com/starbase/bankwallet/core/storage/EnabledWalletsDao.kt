package com.starbase.bankwallet.core.storage

import androidx.room.*
import io.horizontalsystems.bankwallet.entities.EnabledWallet

@Dao
interface EnabledWalletsDao {

    @Query("SELECT * FROM EnabledWallet ORDER BY `walletOrder` ASC")
    fun enabledCoins(): List<EnabledWallet>

    @Query("SELECT * FROM EnabledWallet WHERE accountId = :accountId ORDER BY `walletOrder` ASC")
    fun enabledCoins(accountId: String): List<EnabledWallet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(storableCoin: EnabledWallet)

    @Query("DELETE FROM EnabledWallet")
    fun deleteAll()

    @Query("DELETE FROM EnabledWallet WHERE coinId = :coinId AND accountId = :accountId AND coinSettingsId = :coinSettingsId")
    fun delete(coinId: String, accountId: String, coinSettingsId: String)

    @Transaction
    fun insertWallets(coins: List<EnabledWallet>) {
        coins.forEach { insert(it) }
    }

    @Transaction
    fun deleteWallets(enabledWallets: List<EnabledWallet>) {
        enabledWallets.forEach { delete(it.coinId, it.accountId, it.coinSettingsId) }
    }

}
