package ru.netology.nmedia.activity

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.SignUpBinding
import ru.netology.nmedia.viewmodel.SignViewModel

class SignUpFragment : Fragment() {
    private val authViewModel: SignViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SignUpBinding.inflate(inflater, container, false)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.menu_view_post, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.up -> {
                        findNavController().navigateUp()
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)

        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            if (state.differentPasswords) {
                binding.wrongPassword.isVisible = true
                binding.passwordAccept.setTextColor(Color.RED)
            }
        }
        binding.passwordAccept.setOnClickListener {
            binding.passwordAccept.setTextColor(Color.BLACK)
        }
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
                        authViewModel.changePhoto(uri, uri?.toFile())
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
                    if (authViewModel.photo.value?.file == null) {
                        authViewModel.signUp(log, pass, name, null)
                        if (authViewModel.authState.value!!.errorAddUser) {
                            Snackbar.make(binding.root, "Error add new user", Snackbar.LENGTH_LONG)
                                .show()
                        }
                        findNavController().navigateUp()
                    } else {

                        val file = MultipartBody.Part.createFormData(
                            "file",
                            authViewModel.photo.value!!.file?.name,
                            authViewModel.photo.value!!.file!!.asRequestBody()
                        )
                        authViewModel.signUp(log, pass, name, file)


                        if (authViewModel.authState.value!!.errorAddUser) {
                            Snackbar.make(binding.root, "Error add new user", Snackbar.LENGTH_LONG)
                                .show()
                        }
                        findNavController().navigateUp()

                    }
                } else {
                    authViewModel.wrongPassword()
                }
            }
        }
        return binding.root
    }
}