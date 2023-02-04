package stream.playhuddle.huddle.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import stream.playhuddle.huddle.MainActivity
import stream.playhuddle.huddle.R

private const val NOTIFICATION_ID = 0

@RequiresApi(Build.VERSION_CODES.M)
fun NotificationManager.sendNotification(
    title: String,
    messageBody: String,
    applicationContext: Context
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java,)
        .putExtra(MainActivity.NAVIGATE_TO, "inbox")
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE,
    )
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.huddle_notification_channel_id)
    )
        .setSmallIcon(R.drawable.huddle_logo)
        .setContentTitle(title)
        .setContentText(messageBody)
        .setLargeIcon(
            BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.notification_icon,
            )
        )
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}