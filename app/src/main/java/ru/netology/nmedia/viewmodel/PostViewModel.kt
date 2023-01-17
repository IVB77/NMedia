package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

private val empty = Post(0, "Me", "now", "", false, 0, 0, 0)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    //private val repository: PostRepository = PostRepositorySQLiteImpl(
    //AppDb.getInstance(application).postDao
    //)
    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(context = application).postDao()
    )

    // private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    private val edited = MutableLiveData(empty)
    fun likeById(id: Int) = repository.likeById(id)
    fun shareById(id: Int) = repository.shareById(id)
    fun findById(id: Int) = repository.findById(id)
    fun removeById(id: Int) = repository.removeById(id)
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun changeContent(content: String, id: Int?) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            var idPost = 0
            if (id != null) {
                idPost = id
            }
            edited.value = it.copy(content = text, id = idPost)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

}
