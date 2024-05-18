package com.example.todoapplication.network

import com.example.todoapplication.data.Todo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TodoApiService {
    @GET("todos")
    suspend fun getTodos():List<Todo>
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TodoApiService by lazy {
        retrofit.create(TodoApiService::class.java)
    }
}