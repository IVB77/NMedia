package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        published = "21 мая в 18:36",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        likeByMe = false,
        likes = 999,
        share = 9999,
        views = 1195500
    )
    private val data = MutableLiveData(post)
    override fun get(): LiveData<Post> = data
    override fun like() {
        //post.likeByMe = !post.likeByMe
        //likesQuantity.text =
        //    post.numberConversion(if (post.likeByMe) ++post.likes else --post.likes)
        //likes.setImageResource(if (post.likeByMe) R.drawable.is_liked_24 else R.drawable.ic_baseline_favorite_border_24)
        post = post.copy(
            likes = if (post.likeByMe) post.likes - 1 else post.likes + 1,
            likeByMe = !post.likeByMe
        )
        data.value = post
    }

    override fun share() {
        post = post.copy(share = post.share + 1)
        data.value = post
    }

}