package ru.netology.nmedia.dto

import androidx.room.Embedded


data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String = "",
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int,
    val share: Int,
    val views: Int,
    @Embedded
    var attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
    // val video: String = ""
)

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachType,
)

enum class AttachType {
    IMAGE
}