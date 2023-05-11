package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentViewPostBinding
import ru.netology.nmedia.util.StringArg

class ViewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
        //  var Bundle.idArg: String? by StringArg
    }

    //private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentViewPostBinding.inflate(inflater, container, false)
        val urlMedia = "http://10.0.2.2:9999/media/"
        binding.apply {
            val urlPicture = urlMedia + arguments?.textArg
            Glide.with(Picture)
                .load(urlPicture)
                .timeout(1000)
                .into(Picture)
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

        return binding.root
    }


}
