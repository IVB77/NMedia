package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter({
            viewModel.likeById(it.id)
        }, {
            viewModel.shareById(it.id)
        }, {
            viewModel.removeById(it.id)
        }, {
            binding.close.visibility = View.VISIBLE
            viewModel.edit(it)
        })

        binding.list.adapter = adapter
        binding.close.visibility = View.INVISIBLE
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.edited.observe(this) { post ->
            if (post.id == 0) {
                return@observe
            }
            with(binding.content) {
                requestFocus()
                setText(post.content)
            }
        }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Content can't be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.close.visibility = View.INVISIBLE
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()
                binding.close.visibility = View.INVISIBLE
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
        binding.close.setOnClickListener {
            with(binding.content) {
                viewModel.close()
                binding.close.visibility = View.INVISIBLE
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
        binding.content.setOnClickListener {
            binding.close.visibility = View.VISIBLE
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
