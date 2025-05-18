package com.example.todoapp.data

import com.example.todoapp.data.local.TodoDao
import com.example.todoapp.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class TodoRepository(
    private val apiService: ApiService,
    private val todoDao: TodoDao
) {
    fun getTodos(): Flow<List<Todo>> = flow {
        println("TodoRepository: Starting getTodos flow")
        // Step 1: Check cached todos and insert test todo if empty
        println("TodoRepository: Fetching cached todos")
        val cachedTodos = todoDao.getAllTodos().firstOrNull() ?: emptyList()
        println("TodoRepository: Emitted ${cachedTodos.size} cached todos")
        if (cachedTodos.isEmpty()) {
            println("TodoRepository: Database empty, inserting test todo")
            todoDao.insertAll(listOf(Todo(id = 1, userId = 1, title = "Test Todo", completed = false)))
            println("TodoRepository: Inserted test todo")
            val updatedCachedTodos = todoDao.getAllTodos().firstOrNull() ?: emptyList()
            println("TodoRepository: Emitted ${updatedCachedTodos.size} cached todos after test todo")
            emit(updatedCachedTodos)
        } else {
            emit(cachedTodos)
        }

        // Step 2: Fetch fresh data from network
        println("TodoRepository: Attempting API call")
        try {
            val freshTodos = apiService.getTodos()
            println("TodoRepository: Fetched ${freshTodos.size} todos from API: ${freshTodos.take(1)}")
            todoDao.insertAll(freshTodos)
            println("TodoRepository: Inserted ${freshTodos.size} todos into Room")
        } catch (e: Exception) {
            println("TodoRepository: API error: ${e.message}")
            e.printStackTrace()
        }

        // Step 3: Emit updated cached data
        println("TodoRepository: Fetching updated todos after API")
        val finalTodos = todoDao.getAllTodos().firstOrNull() ?: emptyList()
        println("TodoRepository: Emitted ${finalTodos.size} cached todos after API call")
        emit(finalTodos)
    }

    fun getTodoById(id: Int): Flow<Todo?> {
        return todoDao.getTodoById(id)
    }
}