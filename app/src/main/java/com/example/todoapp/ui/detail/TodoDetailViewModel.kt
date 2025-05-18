package com.example.todoapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TodoDetailUiState(
    val todo: Todo? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class TodoDetailViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TodoDetailUiState())
    val uiState: StateFlow<TodoDetailUiState> = _uiState.asStateFlow()

    fun fetchTodoById(id: Int) {
        viewModelScope.launch {
            repository.getTodoById(id).collect { todo ->
                _uiState.value = TodoDetailUiState(
                    todo = todo,
                    isLoading = false
                )
            }
        }
    }
}