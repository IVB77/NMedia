package ru.netology.nmedia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.UserCommand
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.dto.FeedItem
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
    PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Ad -> R.layout.card_ad
            is Post -> R.layout.post_card
            null -> error("unknown view type ${getItem(position)}")
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.post_card -> {
                val binding =
                    PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, onInteractionListener)
            }
            R.layout.card_ad -> {
                val binding =
                    CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdViewHolder(binding)
            }
            else -> error("unknown view type")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            null -> error("unknown view type")
        }
    }
}

class AdViewHolder(
    private val binding: CardAdBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad) {
        val urlMedia = "http://10.0.2.2:9999/media/"
        binding.apply {
            Glide.with(image)
                .load(("$urlMedia${ad.image}"))
                .timeout(10000)
                .into(image)
        }
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
            if (post.attachment != null) {
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
            menu.isVisible = post.ownedByMe
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

class PostDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }

}