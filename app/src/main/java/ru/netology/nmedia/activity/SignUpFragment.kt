package ru.netology.nmedia.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.auth.AppAuth

import ru.netology.nmedia.databinding.SignUpBinding
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel

class SignUpFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()
    private val viewModelPost: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SignUpBinding.inflate(inflater, container, false)


        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri: Uri? = it.data?.data
                        viewModelPost.changePhoto(uri, uri?.toFile())
                    }
                }
            }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.buttonSignUp.setOnClickListener {
            binding.apply {
                val log: String = login.text.toString()
                val pass: String = password.text.toString()
                val name: String = nameNew.text.toString()
                if (pass == passwordAccept.text.toString()) {
                    if (viewModelPost.photo.value?.file == null) {
                        viewModel.viewModelScope.launch {

                            val authPass = PostsApi.service.signUp(log, pass, name)
                            if (authPass.isSuccessful) {
                                AppAuth.getInstance()
                                    .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                            } else {
                                AppAuth.getInstance().removeAuth()
                            }
                        }

                        findNavController().navigateUp()
                    } else {

                        viewModel.viewModelScope.launch {

                            val file = MultipartBody.Part.createFormData("file",
                               viewModelPost.photo.value!!.file?.name, viewModelPost.photo.value!!.file!!.asRequestBody())
                            val authPass = PostsApi.service.signUpWithAvatar(log, pass, name, file)

                            if (authPass.isSuccessful) {
                                AppAuth.getInstance()
                                    .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                            } else {
                                AppAuth.getInstance().removeAuth()
                            }
                        }

                        findNavController().navigateUp()

                    }
                } else {
                    Snackbar.make(binding.root, "Passwords don't match", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
        return binding.root
    }
}