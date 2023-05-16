package ru.netology.nmedia.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    private val channelId = "remote"

    @Inject
    lateinit var appAuth: AppAuth

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
        val checkToken = appAuth.checkAuth(messageAuth.recipientId)

        if (checkToken.isNullOrBlank()) {

            sendNotification(messageAuth)


        } else {
            appAuth.sendPushToken(checkToken)
        }
    }

    override fun onNewToken(token: String) {
        appAuth.sendPushToken(token)
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

