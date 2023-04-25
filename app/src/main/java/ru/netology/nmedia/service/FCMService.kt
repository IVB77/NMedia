package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth


class FCMService : FirebaseMessagingService() {

    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

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
        // TODO: replace this in homework
        println(message.data["content"])
        /* message.data[action]?.let {
            try {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(
                        gson.fromJson(
                            message.data[content],
                            Like::class.java
                        )
                    )
                    Action.NEW -> newPost(gson.fromJson(message.data[content], New::class.java))
                }
            } catch (e: Exception) {
                otherMessage(gson.fromJson(message.data[content], Other::class.java))
            }


        }*/
    }

    override fun onNewToken(token: String) {
        AppAuth.getInstance().sendPushToken(token)
    }
}

/*private fun handleLike(content: Like) {
    val notification = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(
            getString(
                R.string.notification_user_liked,
                content.userName,
                content.postAuthor,
            )
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    NotificationManagerCompat.from(this)
        .notify(Random.nextInt(100_000), notification)
}

private fun otherMessage(content: Other) {
    val notification = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(
            getString(
                R.string.notification_user_other,
                content.userName
            )
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    NotificationManagerCompat.from(this)
        .notify(Random.nextInt(100_000), notification)
}

private fun newPost(content: New) {

    val notification = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(
            getString(
                R.string.notification_user_new,
                content.userName
            )
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setStyle(NotificationCompat.BigTextStyle().bigText(content.postContent))
        .build()

    NotificationManagerCompat.from(this)
        .notify(Random.nextInt(100_000), notification)
}
}

enum class Action {
LIKE, NEW
}

data class Like(
val userId: Long,
val userName: String,
val postId: Long,
val postAuthor: String,
)

data class Other(
val userId: Long,
val userName: String,
val postId: Long,
)

data class New(
val userName: String,
val postContent: String,
)*/
