package com.starbase.core

import java.util.*

class CurrentDateProvider : ICurrentDateProvider {

    override val currentDate: Date
        get() = Date()

}
