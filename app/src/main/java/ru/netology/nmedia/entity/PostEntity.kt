package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val share: Int = 0,
    val views: Int = 0,
    @Embedded
    val attachment: Attachment?,
    val newPost: Boolean = false,
    //val video: String = "",
) {
    fun toDto() =
        Post(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            published = published,
            content = content,
            likedByMe = likedByMe,
            likes = likes,
            share = share,
            views = views,
            attachment = attachment
        )//, video)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                id = dto.id,
                authorId = dto.authorId,
                author = dto.author,
                authorAvatar = dto.authorAvatar,
                published = dto.published,
                content = dto.content,
                likedByMe = dto.likedByMe,
                likes = dto.likes,
                share = dto.share,
                views = dto.views,
                attachment = dto.attachment
                // dto.video
            )

    }
}

//fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)