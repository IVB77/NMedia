package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.model.AuthModel
import ru.netology.nmedia.model.PhotoModel
import java.io.File
import javax.inject.Inject

private val noPhoto = PhotoModel()
@HiltViewModel
class SignViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val apiService: PostsApiService
    ): ViewModel() {
    private val _authState = MutableLiveData<AuthModel>()
    val authState: LiveData<AuthModel>
        get() = _authState
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    fun signIn(log: String, pass: String) {
        runBlocking {
            val authPass = apiService.signIn(log, pass)
            if (authPass.isSuccessful) {
                appAuth
                    .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                _authState.value = AuthModel(wrongLogin = false)
            } else {
                appAuth.removeAuth()
                _authState.value = AuthModel(wrongLogin = true)
            }
        }
    }

    fun signUp(log: String, pass: String, name: String, file: MultipartBody.Part?) {
        if (file == null) {
            runBlocking {
                val authPass = apiService.signUp(log, pass, name)
                if (authPass.isSuccessful) {
                    appAuth
                        .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                    _authState.value = AuthModel(errorAddUser = false)
                } else {
                    appAuth.removeAuth()
                    _authState.value = AuthModel(errorAddUser = true)
                }
            }
        } else {
            runBlocking {
                val authPass = apiService.signUpWithAvatar(
                    log,
                    pass,
                    name,
                    file
                )

                if (authPass.isSuccessful) {
                    appAuth
                        .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                    _authState.value = AuthModel(errorAddUser = false)
                } else {
                    appAuth.removeAuth()
                    _authState.value = AuthModel(errorAddUser = true)
                }
            }
        }

    }

    fun wrongPassword() {
        _authState.value = AuthModel(differentPasswords = true)
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }
}