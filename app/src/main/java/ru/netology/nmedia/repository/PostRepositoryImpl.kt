package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post


class PostRepositoryImpl : PostRepository {

    override fun getAll(callback: PostRepository.Callback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(java.lang.RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun likeById(post: Post, callback: PostRepository.Callback<Post>) {
        if (!post.likedByMe) {
            PostsApi.retrofitService.likeById(post.id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(java.lang.RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(
                        response.body()
                            ?: throw RuntimeException("Error request to server ${response.code()} ${response.message()}")
                    )
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }

            })

        } else {
            PostsApi.retrofitService.dislikeById(post.id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(java.lang.RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(
                        response.body()
                            ?: throw RuntimeException("Error request to server ${response.code()} ${response.message()}")
                    )
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }

            })
        }
    }

    override fun shareById(id: Long) {


    }

    override fun save(post: Post, callback: PostRepository.Callback<Any>) {

        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(java.lang.RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(
                    response.body()
                        ?: throw RuntimeException("Error request to server ${response.code()} ${response.message()}")
                )
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Any>) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(java.lang.RuntimeException(response.message()))
                    return
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t)
            }

        })

    }

    override fun findById(id: Long): Post {

        return findById(id)
    }

}