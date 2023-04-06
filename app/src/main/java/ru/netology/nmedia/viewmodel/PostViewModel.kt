package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.activity.FeedModel
import ru.netology.nmedia.activity.FeedModelState
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException


private val empty = Post(0L, "Me", "", 28022023L, "", false, 0, 0, 0)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(AppDb.getInstance(context = application).postDao())
    val data: LiveData<FeedModel> = repository.data.map (::FeedModel)
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState


    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun refresh() {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun likeById(id: Long) {
        /*val likePost = _data.value!!.posts.find { it.id == id }!!
        repository.likeById(likePost, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                _data.postValue(
                    FeedModel(
                        _data.value?.posts.orEmpty().map { if (it.id == id) posts else it })
                )
            }

            override fun onError(t: Throwable) {
                _data.postValue(FeedModel(error = true))
            }
        })*/


    }

    fun shareById(id: Long) {}//= repository.shareById(id)
    suspend fun findById(id: Long)  = repository.findById(id)
    fun removeById(id: Long) {
    /*    val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )
        try {
            repository.removeById(id, object : PostRepository.Callback<Any> {
                override fun onSuccess(posts: Any) {}

                override fun onError(t: Throwable) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        } catch (e: IOException) {
            _data.postValue(_data.value?.copy(posts = old))
        }*/
    }


    fun save() {
       /* edited.value?.let {

            repository.save(it, object : PostRepository.Callback<Any> {
                override fun onSuccess(posts: Any) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(t: Throwable) {
                    _data.postValue(FeedModel(error = true))
                }
            })


        }
        edited.value = empty*/
    }

    fun changeContent(content: String, id: Long?) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun edit(post: Post) {
        edited.value = post
    }

}
