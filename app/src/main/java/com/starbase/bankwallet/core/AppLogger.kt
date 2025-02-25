package com.starbase.bankwallet.core

import java.util.*

class AppLogger(private val scope: List<String> = listOf()) {

    constructor(group: String) : this(listOf(group))

    private val actionId: String
        get() = scope.joinToString(":")

    fun getScopedUnique() : AppLogger {
        return getScoped(UUID.randomUUID().toString())
    }

    fun getScoped(scope: String) : AppLogger {
        return AppLogger(this.scope + scope)
    }

    fun info(message: String) {
        com.starbase.bankwallet.core.AppLog.info(actionId, message)
    }

    fun warning(message: String, e: Throwable) {
        com.starbase.bankwallet.core.AppLog.warning(actionId, message, e)
    }
}