package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
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
        var idPost = 0L
        if (arguments?.idArg != null) {
            idPost = requireArguments().idArg!!.toLong()
        } else {
            findNavController().navigateUp()
        }
        viewModel.viewModelScope.launch {
            val post = viewModel.data.value?.posts?.find { it.id == idPost }
            if (post != null) {
                binding.apply {
                    content.text = post.content
                    author.text = post.author
                    published.text = post.published.toString()
                    content.text = post.content
                    likes.text = UserCommand.numberConversion(post.likes)
                    share.text = UserCommand.numberConversion(post.share)
                    viewQuantity.text = UserCommand.numberConversion(post.views)
                    likes.isChecked = post.likedByMe


                    share.setOnClickListener {
                        viewModel.viewModelScope.launch {
                            viewModel.shareById(post.id)
                            share.text = UserCommand.numberConversion(post.share)
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, post.content)
                                type = "text/plain"
                            }
                            val shareIntent =
                                Intent.createChooser(intent, getString(R.string.chooser_share_post))
                            startActivity(shareIntent)
                        }
                    }
                    author.setOnClickListener {
                        findNavController().navigateUp()
                    }

                    published.setOnClickListener {
                        findNavController().navigateUp()
                    }

                    menu.setOnClickListener {
                        PopupMenu(it.context, it).apply {
                            inflate(R.menu.options_post)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.remove -> {
                                        if (arguments?.idArg != null) {
                                            viewModel.removeById(post.id)
                                        }
                                        findNavController().navigateUp()
                                        true
                                    }
                                    R.id.edit -> {
                                        findNavController().navigateUp()
                                        findNavController().navigate(
                                            R.id.action_feedFragment_to_newPostFragment,
                                            Bundle().apply
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
                }
            }

        }
        return binding.root
    }


}
