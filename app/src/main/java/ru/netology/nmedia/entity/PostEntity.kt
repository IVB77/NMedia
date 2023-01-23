package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likeByMe: Boolean,
    val likes: Int = 0,
    val share: Int = 0,
    val views: Int = 0,
    val video: String = "",
) {
    fun toDto() = Post(id, author, published, content, likeByMe, likes, share, views, video)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.published,
                dto.content,
                dto.likeByMe,
                dto.likes,
                dto.share,
                dto.views,
                dto.video
            )

    }
}
