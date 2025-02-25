package com.starbase.bankwallet.entities

import io.horizontalsystems.coinkit.models.CoinType

data class InitialSyncSetting(val coinType: CoinType,
                              var syncMode: SyncMode)
