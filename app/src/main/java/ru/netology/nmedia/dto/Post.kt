package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val published: Long,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int,
    val share: Int,
    val views: Int,
    // val video: String = ""
)