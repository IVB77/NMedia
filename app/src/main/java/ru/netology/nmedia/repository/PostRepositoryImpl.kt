package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val typeTokenPost = object : TypeToken<Post>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(callback: PostRepository.GetAllCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()
        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: java.lang.Exception) {
                        callback.onError(e)
                    }
                }
            })
    }


    override fun likeById(post: Post, callback: PostRepository.GetAllCallback<Post>) {
        //dao.likeById(id)
        if (!post.likedByMe) {
            val request: Request = Request.Builder()
                .post(gson.toJson(post).toRequestBody(jsonType))
                .url("${BASE_URL}/api/posts/${post.id}/likes")
                .build()

            try {
                client.newCall(request)
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            callback.onError(e)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (!response.isSuccessful) {
                                throw IOException(
                                    "Запрос к серверу не был успешен:" +
                                            " ${response.code} ${response.message}"
                                )
                            }
                            callback.onSuccess(
                                gson.fromJson(
                                    response.body!!.string(),
                                    typeTokenPost
                                )
                            )


                        }
                    })
            } catch (e: IOException) {
                println("Ошибка подключения: $e")
            }


        } else {
            val request: Request = Request.Builder()
                .delete()
                .url("${BASE_URL}/api/posts/${post.id}/likes")
                .build()

            try {
                client.newCall(request)
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            callback.onError(e)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (!response.isSuccessful) {
                                throw IOException(
                                    "Запрос к серверу не был успешен:" +
                                            " ${response.code} ${response.message}"
                                )
                            }
                            callback.onSuccess(
                                gson.fromJson(
                                    response.body!!.string(),
                                    typeTokenPost
                                )
                            )

                        }
                    })
            } catch (e: IOException) {
                println("Ошибка подключения: $e")
            }
        }
    }

    override fun shareById(id: Long) {
        //dao.shareById(id)

    }

    override fun save(post: Post, callback: PostRepository.GetAllCallback<Any>) {
        //dao.save(PostEntity.fromDto(post))
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(post)
                }
            })
    }

    override fun removeById(id: Long, callback: PostRepository.GetAllCallback<Any>) {
        // dao.removeById(id)
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {}
            })

    }

    override fun findById(id: Long): Post {

        return findById(id)
    }

}