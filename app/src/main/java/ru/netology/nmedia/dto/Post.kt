package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likeByMe: Boolean,
    var likes: Int,
    var share: Int,
    var views: Int
) {

    fun numberConversion(number: Int): String {

        return if (number < 1_000) {
            number.toString()
        } else if (number < 10_000) {
            (number / 1000).toString() + (if ((number % 1000 - number % 1000 % 100) == 0) "" else ("." + ((number % 1000 - number % 1000 % 100) / 100))).toString() + "K"
        } else if (number < 1_000_000) {
            ((number - number % 1000) / 1000).toString() + "K"
        } else {
            (number / 1_000_000).toString() + (if ((number % 1_000_000 - number % 1_000_000 % 100_000) == 0) "" else ("." + ((number % 1_000_000 - number % 1_000_000 % 100_000) / 100_000))).toString() + "M"
        }

    }
}