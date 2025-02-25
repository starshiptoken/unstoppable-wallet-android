package com.starbase.bankwallet.core.managers

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.horizontalsystems.bankwallet.R
import com.starbase.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.ILocalStorage
import io.horizontalsystems.bankwallet.core.INotificationManager
import io.horizontalsystems.bankwallet.core.providers.Translator
import io.horizontalsystems.bankwallet.entities.AlertNotification
import io.horizontalsystems.bankwallet.modules.launcher.LauncherActivity
import io.horizontalsystems.core.BackgroundManager
import android.app.NotificationManager as SystemNotificationManager

class NotificationManager(
        private val androidNotificationManager: NotificationManagerCompat,
        private val localStorage: ILocalStorage
        ) : INotificationManager, BackgroundManager.Listener {

    override val enabledInPhone: Boolean
        get() = when {
            !androidNotificationManager.areNotificationsEnabled() -> false
            else -> {
                val notificationChannel = androidNotificationManager.getNotificationChannel(channelId)
                notificationChannel?.importance != NotificationManagerCompat.IMPORTANCE_NONE
            }
        }

    override val enabled: Boolean
        get() {
            return enabledInPhone && localStorage.isAlertNotificationOn
        }

    override fun willEnterForeground() {
        super.willEnterForeground()
        clear()
    }

    override fun clear() {
        androidNotificationManager.cancelAll()
    }

    override fun show(notification: AlertNotification) {
        createNotificationChannel()
        showNotification(notification)
    }

    private fun showNotification(notification: AlertNotification) {
        val builder = NotificationCompat.Builder(com.starbase.bankwallet.core.App.instance, channelId)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(notification.body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)

        androidNotificationManager.notify(notification.id, builder.build())
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(com.starbase.bankwallet.core.App.instance, LauncherActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(com.starbase.bankwallet.core.App.instance, 0, intent, 0)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Translator.getString(R.string.App_Name)
            val importance = SystemNotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            // Register the channel with the system
            androidNotificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val channelId = "priceAlert"
    }
}
