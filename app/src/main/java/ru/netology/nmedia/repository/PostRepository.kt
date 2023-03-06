package ru.netology.nmedia.repository


import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(callback: GetAllCallback<List<Post>>)
    fun likeById(post: Post, callback: GetAllCallback<Post>)
    fun shareById(id: Long)
    fun removeById(id: Long, callback: GetAllCallback<Any>)
    fun save(post: Post, callback: GetAllCallback<Any>)
    fun findById(id: Long): Post

    interface GetAllCallback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: java.lang.Exception) {}
    }
}