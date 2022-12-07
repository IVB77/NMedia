package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likeByMe = false,
            likes = 1099,
            share = 999995,
            views = 1195500
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesQuantity.text = post.numberConversion(post.likes)
            sharingQuantity.text = post.numberConversion(post.share)
            viewQuantity.text = post.numberConversion(post.views)
            if (post.likeByMe) {
                likes.setImageResource(R.drawable.is_liked_24)
            }

            likes.setOnClickListener {
                post.likeByMe = !post.likeByMe
                likesQuantity.text = post.numberConversion(if (post.likeByMe) ++post.likes else --post.likes)
                likes.setImageResource(if (post.likeByMe) R.drawable.is_liked_24 else R.drawable.ic_baseline_favorite_border_24)
            }
            share.setOnClickListener {
                sharingQuantity.text = post.numberConversion(++post.share)
            }

        }
    }
}