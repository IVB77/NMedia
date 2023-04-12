package ru.netology.nmedia.repository


import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAll()
    suspend fun likeById(id: Long)
    suspend fun shareById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: Post)
    suspend fun findById(id: Long): Post
    fun getNewer(id: Long):Flow<Int>
    suspend fun allOld()


}