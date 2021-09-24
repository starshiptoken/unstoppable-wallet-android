package com.starbase.bankwallet.core.managers

import android.app.Activity
import android.app.KeyguardManager
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import com.starbase.bankwallet.BuildConfig
import com.starbase.bankwallet.core.App
import io.horizontalsystems.core.ISystemInfoManager

class SystemInfoManager : ISystemInfoManager {

    override val appVersion: String = com.starbase.bankwallet.BuildConfig.VERSION_NAME

    private val biometricManager by lazy { BiometricManager.from(com.starbase.bankwallet.core.App.instance) }

    override val isSystemLockOff: Boolean
        get() {
            val keyguardManager = com.starbase.bankwallet.core.App.instance.getSystemService(Activity.KEYGUARD_SERVICE) as KeyguardManager
            return !keyguardManager.isDeviceSecure
        }

    override val biometricAuthSupported: Boolean
        get() = biometricManager.canAuthenticate() == BIOMETRIC_SUCCESS

    override val deviceModel: String
        get() = "${Build.MANUFACTURER} ${Build.MODEL}"

    override val osVersion: String
        get() = "Android ${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})"

}
