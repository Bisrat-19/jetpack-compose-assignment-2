package com.example.todoapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TodoListUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TodoListUiState())
    val uiState: StateFlow<TodoListUiState> = _uiState.asStateFlow()

    init {
        fetchTodos()
    }

    private fun fetchTodos() {
        viewModelScope.launch {
            repository.getTodos().collect { todos ->
                println("TodoListViewModel: Received ${todos.size} todos")
                _uiState.value = TodoListUiState(
                    todos = todos,
                    isLoading = false
                )
            }
        }
    }
}