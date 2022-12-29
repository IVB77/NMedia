package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likeByMe: Boolean,
    val likes: Int,
    val share: Int,
    val views: Int,
    val video: String = ""
)