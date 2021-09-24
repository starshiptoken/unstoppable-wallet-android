package com.starbase.bankwallet.core.providers

import androidx.annotation.StringRes
import com.starbase.bankwallet.core.App

object Translator {

    fun getString(@StringRes id: Int): String {
        return com.starbase.bankwallet.core.App.instance.localizedContext().getString(id)
    }

    fun getString(@StringRes id: Int, vararg params: Any): String {
        return com.starbase.bankwallet.core.App.instance.localizedContext().getString(id, *params)
    }
}
