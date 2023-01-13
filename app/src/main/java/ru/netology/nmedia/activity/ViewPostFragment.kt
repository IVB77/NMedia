package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentViewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.util.StringArg

class ViewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
        var Bundle.idArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentViewPostBinding.inflate(inflater, container, false)
        arguments?.textArg?.let(binding.contentView::setText)

        binding.ok.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            if (arguments?.idArg != null) {
                                val idPost: Int = requireArguments().idArg!!.toInt()
                                viewModel.removeById(idPost)
                            }
                            findNavController().navigateUp()
                            true
                        }
                        R.id.edit -> {
                            findNavController().navigateUp()
                            findNavController().navigate(
                                R.id.action_feedFragment_to_newPostFragment, Bundle().apply
                                {
                                    textArg = arguments?.textArg
                                    idArg = arguments?.idArg
                                })
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
        return binding.root
    }


}
