package ru.netology.nmedia.repository


import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(callback: Callback<List<Post>>)
    fun likeById(post: Post, callback: Callback<Post>)
    fun shareById(id: Long)
    fun removeById(id: Long, callback: Callback<Any>)
    fun save(post: Post, callback: Callback<Any>)
    fun findById(id: Long): Post

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(t: Throwable) {}
    }
}