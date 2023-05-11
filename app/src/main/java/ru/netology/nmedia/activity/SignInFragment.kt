package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.SignInBinding
import ru.netology.nmedia.viewmodel.SignViewModel


class SignInFragment : Fragment() {
    private val viewModel: SignViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SignInBinding.inflate(inflater, container, false)
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            if (state.wrongLogin) {
                binding.wrongLogin.isVisible = true
                binding.login.text.clear()
                binding.password.text.clear()
            }
        }
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

        binding.buttonSignIn.setOnClickListener {
            binding.apply {
                val log: String = login.text.toString()
                val pass: String = password.text.toString()
                viewModel.signIn(log, pass)
                if (!viewModel.authState.value!!.wrongLogin) {
                    findNavController().navigateUp()
                }
            }
        }
        return binding.root
    }
}