package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.idArg
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onEdit(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment, Bundle().apply
                    {
                        textArg = post.content
                        idArg = post.id.toString()
                    })
                viewModel.edit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onVideoGroup(post: Post) {
                val url = post.video
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }

            override fun onContent(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_viewPostFragment2,
                    Bundle().apply
                    {
                        textArg = post.content
                        idArg = post.id.toString()
                    })
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner)
        { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {

            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment
            )
        }
        return binding.root
    }

}


object UserCommand {
    fun numberConversion(number: Int): String {
        return if (number < 1_000) {
            number.toString()
        } else if (number < 10_000) {
            (number / 1000).toString() + (if ((number % 1000 - number % 1000 % 100) == 0) "" else ("." + ((number % 1000 - number % 1000 % 100) / 100))).toString() + "K"
        } else if (number < 1_000_000) {
            ((number - number % 1000) / 1000).toString() + "K"
        } else {
            (number / 1_000_000).toString() + (if ((number % 1_000_000 - number % 1_000_000 % 100_000) == 0) "" else ("." + ((number % 1_000_000 - number % 1_000_000 % 100_000) / 100_000))).toString() + "M"
        }
    }
}
