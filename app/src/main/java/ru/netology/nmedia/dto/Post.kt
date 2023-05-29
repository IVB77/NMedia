package ru.netology.nmedia.dto

import androidx.room.Embedded

sealed interface FeedItem {
    val id: Long
}

data class Post(
    override val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String = "",
    val published: Long,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int,
    val share: Int,
    val views: Int,
    @Embedded
    var attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
    // val video: String = ""
) : FeedItem

data class Ad(
    override val id: Long,
    val url: String,
    val image: String,
) : FeedItem

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachType,
)

enum class AttachType {
    IMAGE
}