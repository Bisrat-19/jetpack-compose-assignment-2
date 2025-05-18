package com.example.todoapp.data.network

import com.example.todoapp.data.Todo
import retrofit2.http.GET

interface ApiService {
    @GET("todos")
    suspend fun getTodos(): List<Todo>
}