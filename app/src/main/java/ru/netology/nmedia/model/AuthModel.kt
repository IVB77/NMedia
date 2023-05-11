package ru.netology.nmedia.model

data class AuthModel(
    val wrongLogin :Boolean = false,
    val differentPasswords :Boolean = false,
    val errorAddUser :Boolean = false,

)
