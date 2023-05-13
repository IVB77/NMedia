package ru.netology.nmedia.di

import android.content.Context
import androidx.room.Room
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class DependencyContainer(
    private val context: Context
) {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"

        @Volatile
        private var instance: DependencyContainer? = null

        fun initApp(context: Context) {
            instance = DependencyContainer(context)
        }

        fun getInstance(): DependencyContainer {
            return instance!!
        }
    }


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val appBd = Room.databaseBuilder(context, AppDb::class.java, "app13.db")
        .fallbackToDestructiveMigration()
        .build()

    val apiService = retrofit.create<PostsApiService>()

    private val postDao = appBd.postDao()

    val repository: PostRepository = PostRepositoryImpl(
        postDao,
        apiService,
    )

    val appAuth = AppAuth(context)
}