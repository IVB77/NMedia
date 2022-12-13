package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->

            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likesQuantity.text = UserCommand.numberConversion(post.likes)
                sharingQuantity.text = UserCommand.numberConversion(post.share)
                viewQuantity.text = UserCommand.numberConversion(post.views)
                likes.setImageResource(if (post.likeByMe) R.drawable.is_liked_24 else R.drawable.ic_baseline_favorite_border_24)

                likes.setOnClickListener {
                    viewModel.like()
                }

                share.setOnClickListener {
                    viewModel.share()
                }
            }
        }
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