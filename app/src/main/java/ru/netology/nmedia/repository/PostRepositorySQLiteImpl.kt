package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun findById(id: Int): Post? {
        return posts.find { post -> post.id == id }
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likeByMe = !it.likeByMe,
                likes = if (it.likeByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Int) {
        dao.shareById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(share = it.share + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}