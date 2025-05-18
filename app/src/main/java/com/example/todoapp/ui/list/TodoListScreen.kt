package com.example.todoapp.ui.list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.ui.TodoListViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    repository: TodoRepository,
    onTodoClick: (Int) -> Unit,
    viewModel: TodoListViewModel = viewModel(factory = TodoListViewModelFactory(repository))
) {
    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: open add-todo flow */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add todo")
            }
        }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingBox(Modifier.padding(padding))
            uiState.error != null -> ErrorBox(uiState.error, Modifier.padding(padding))
            uiState.todos.isEmpty() -> EmptyBox(Modifier.padding(padding))
            else -> TodoList(uiState.todos, onTodoClick, Modifier.padding(padding))
        }
    }
}

/* ---------- internal helpers ---------- */

@Composable
private fun TodoList(
    todos: List<Todo>,
    onTodoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
        items(todos) { todo ->
            TodoCard(todo, onTodoClick)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun TodoCard(todo: Todo, onTodoClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { onTodoClick(todo.id) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                todo.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            AssistChip(
                onClick = { /* no-op for now */ },
                label = { Text(if (todo.completed) "Done" else "Open") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (todo.completed)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    labelColor = if (todo.completed)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary,
                ),
                shape = MaterialTheme.shapes.small
            )
        }
    }
}

/* ---------- reusable state boxes ---------- */

@Composable
private fun LoadingBox(modifier: Modifier = Modifier) =
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }

@Composable
private fun ErrorBox(msg: String, modifier: Modifier = Modifier) =
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            "Error: $msg",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }

@Composable
private fun EmptyBox(modifier: Modifier = Modifier) =
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            "No todos yet",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
