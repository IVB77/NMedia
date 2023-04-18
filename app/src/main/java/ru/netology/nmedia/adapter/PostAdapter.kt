package ru.netology.nmedia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.UserCommand
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post


interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onVideoGroup(post: Post) {}
    fun onContent(post: Post) {}
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding, onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("CheckResult")
    fun bind(post: Post) {
        val urlAvatars = "http://10.0.2.2:9999/avatars/"
        //val urlImages = "http://10.0.2.2:9999/images/"
        val urlMedia = "http://10.0.2.2:9999/media/"
        binding.apply {
            author.text = post.author

            Glide.with(avatar)
                .load("$urlAvatars${post.authorAvatar}")
                .timeout(10000)
                .circleCrop()
                .into(avatar)
            /*if (post.attachment != null) {
                videoPicture.visibility = View.VISIBLE
                Glide.with(videoPicture)
                    .load("$urlImages${post.attachment!!.url}")
                    .timeout(10000)
                    .into(videoPicture)
            }*/

            published.text = post.published.toString()
            content.text = post.content
            likes.text = UserCommand.numberConversion(post.likes)
            share.text = UserCommand.numberConversion(post.share)
            viewQuantity.text = UserCommand.numberConversion(post.views)
            likes.isChecked = post.likedByMe
            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            if (post.attachment!=null) {
                videoPicture.visibility = View.VISIBLE
                val urlPicture = urlMedia + post.attachment!!.url
                Glide.with(videoPicture)
                    .load(urlPicture)
                    .timeout(10000)
                    .into(videoPicture)
            } else {
                videoGroup.visibility = View.GONE
            }
            videoPicture.setOnClickListener {
                onInteractionListener.onVideoGroup(post)
            }
            /*play.setOnClickListener {
                onInteractionListener.onVideoGroup(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            content.setOnClickListener {
                onInteractionListener.onContent(post)
            }*/
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
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