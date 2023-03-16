package ru.netology.nmedia.dto

import ru.netology.nmedia.enumeration.AttachType


data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String = "",
    val published: Long,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int,
    val share: Int,
    val views: Int,
    var attachment: Attachment? = null,
    // val video: String = ""
)

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachType,
)