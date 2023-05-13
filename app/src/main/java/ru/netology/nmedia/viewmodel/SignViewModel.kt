package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.model.AuthModel
import ru.netology.nmedia.model.PhotoModel
import java.io.File

private val noPhoto = PhotoModel()

class SignViewModel() : ViewModel() {
    private val _authState = MutableLiveData<AuthModel>()
    val authState: LiveData<AuthModel>
        get() = _authState
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    fun signIn(log: String, pass: String) {
        runBlocking {
            val authPass = DependencyContainer.getInstance().apiService.signIn(log, pass)
            if (authPass.isSuccessful) {
                DependencyContainer.getInstance().appAuth
                    .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                _authState.value = AuthModel(wrongLogin = false)
            } else {
                DependencyContainer.getInstance().appAuth.removeAuth()
                _authState.value = AuthModel(wrongLogin = true)
            }
        }
    }

    fun signUp(log: String, pass: String, name: String, file: MultipartBody.Part?) {
        if (file == null) {
            runBlocking {
                val authPass = DependencyContainer.getInstance().apiService.signUp(log, pass, name)
                if (authPass.isSuccessful) {
                    DependencyContainer.getInstance().appAuth
                        .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                    _authState.value = AuthModel(errorAddUser = false)
                } else {
                    DependencyContainer.getInstance().appAuth.removeAuth()
                    _authState.value = AuthModel(errorAddUser = true)
                }
            }
        } else {
            runBlocking {
                val authPass = DependencyContainer.getInstance().apiService.signUpWithAvatar(log, pass, name, file)

                if (authPass.isSuccessful) {
                    DependencyContainer.getInstance().appAuth
                        .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                    _authState.value = AuthModel(errorAddUser = false)
                } else {
                    DependencyContainer.getInstance().appAuth.removeAuth()
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