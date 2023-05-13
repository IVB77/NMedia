package ru.netology.nmedia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.activity.FeedModel
import ru.netology.nmedia.activity.FeedModelState
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File


private val empty = Post(
    id = 0L,
    authorId = 0,
    author = "Student",
    authorAvatar = "",
    published = 28022023L,
    content = "",
    likedByMe = false,
    likes = 0,
    share = 0,
    views = 0
)
private val noPhoto = PhotoModel()


class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        DependencyContainer.getInstance().repository
    val data: LiveData<FeedModel> = DependencyContainer.getInstance().appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            repository.data
                .map { posts ->
                    FeedModel(
                        posts.map { it.copy(ownedByMe = it.authorId == myId) },
                        posts.isEmpty()
                    )
                }
        }
        .asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState


    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    //val ppp : Long = repository.getLastPost().toString().toLong()
    val newer: LiveData<Int> = data.switchMap {
        repository.getNewer(it.posts.firstOrNull()?.id ?: 0L).asLiveData(Dispatchers.Default)
    }

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

    fun allOld() = viewModelScope.launch {
        repository.allOld()
    }

    fun checkForSignIn():Boolean = DependencyContainer.getInstance().appAuth.authStateFlow.value.token.isNullOrBlank()

    fun likeById(id: Long) = viewModelScope.launch {
        try {
            repository.likeById(id)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }

    }

    fun shareById(id: Long) {}//= repository.shareById(id)
    fun removeById(id: Long) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.removeById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    when (_photo.value) {
                        noPhoto -> repository.save(it)
                        else -> _photo.value?.file?.let { file ->
                            repository.saveWithAttachment(it, MediaUpload(file))
                        }
                    }
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = empty
        _photo.value = noPhoto
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun edit(post: Post) {
        //edited.value = post
    }

}
