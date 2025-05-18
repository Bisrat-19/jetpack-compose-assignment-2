package com.example.todoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoapp.data.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todos: List<Todo>)

    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM todos WHERE id = :id")
    fun getTodoById(id: Int): Flow<Todo?>
}