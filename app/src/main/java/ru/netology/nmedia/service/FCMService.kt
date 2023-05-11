package ru.netology.nmedia.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import okhttp3.internal.notify
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState

class FCMService : FirebaseMessagingService() {

    private val channelId = "remote"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val gson = Gson()

        val messageAuth =
            gson.fromJson(message.data["content"], MessageIn::class.java)
        val checkToken = AppAuth.getInstance().checkAuth(messageAuth.recipientId)

        if (checkToken.isNullOrBlank()) {

            sendNotification(messageAuth)


        } else {
            AppAuth.getInstance().sendPushToken(checkToken)
        }
    }

    override fun onNewToken(token: String) {
        AppAuth.getInstance().sendPushToken(token)
    }

    private fun sendNotification(messageIn: MessageIn) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_for_recipient,
                    messageIn.recipientId.toString(),
                    messageIn.content,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        NotificationManagerCompat.from(this).notify(5, notification)


    }


}

data class MessageIn(val recipientId: Long?, val content: String)

