package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.SignInBinding
import ru.netology.nmedia.viewmodel.AuthViewModel


class SignInFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SignInBinding.inflate(inflater, container, false)


        binding.buttonSignIn.setOnClickListener {
            binding.apply {
                val log: String = login.text.toString()
                val pass: String = password.text.toString()
                viewModel.viewModelScope.launch {

                    val authPass = PostsApi.service.signIn(log, pass)
                    if (authPass.isSuccessful) {
                        AppAuth.getInstance()
                            .setAuth(authPass.body()!!.id, authPass.body()!!.token!!)
                    } else {
                        AppAuth.getInstance().setAuth(0, null)
                    }
                }
            }
            findNavController().navigateUp()
        }
        return binding.root
    }
}