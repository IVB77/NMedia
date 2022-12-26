package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.UserCommand
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post

typealias OnClickListener = (post: Post) -> Unit


class PostAdapter(
    private val onLikeListener: OnClickListener,
    private val onShareListener: OnClickListener,
    private val onRemoveListener: OnClickListener,
    private val onEditListener: OnClickListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding,
            onLikeListener,
            onShareListener,
            onRemoveListener,
            onEditListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onLikeListener: OnClickListener,
    private val onShareListener: OnClickListener,
    private val onRemoveListener: OnClickListener,
    private val onEditListener: OnClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes.text = UserCommand.numberConversion(post.likes)
            share.text = UserCommand.numberConversion(post.share)
            viewQuantity.text = UserCommand.numberConversion(post.views)
            likes.isChecked = post.likeByMe
            likes.setOnClickListener {
                onLikeListener(post)
            }
            share.setOnClickListener {
                onShareListener(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onRemoveListener(post)
                                true
                            }
                            R.id.edit -> {
                                onEditListener(post)
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

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}