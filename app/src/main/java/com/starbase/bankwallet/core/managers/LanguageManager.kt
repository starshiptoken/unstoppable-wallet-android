package com.starbase.bankwallet.core.managers

import com.starbase.bankwallet.core.App
import io.horizontalsystems.core.ILanguageManager
import java.util.*

class LanguageManager : ILanguageManager {

    override var fallbackLocale: Locale = Locale.ENGLISH

    override var currentLocale: Locale = com.starbase.bankwallet.core.App.instance.getLocale()
        set(value) {
            field = value

            com.starbase.bankwallet.core.App.instance.setLocale(currentLocale)
        }

    override var currentLanguage: String
        get() = currentLocale.language
        set(value) {
            currentLocale = Locale(value)
        }

    override val currentLanguageName: String
        get() = currentLocale.displayLanguage.capitalize()

    override fun getName(language: String): String {
        return Locale(language).displayLanguage.capitalize()
    }

    override fun getNativeName(language: String): String {
        val locale = Locale(language)
        return locale.getDisplayLanguage(locale).capitalize()
    }

}
