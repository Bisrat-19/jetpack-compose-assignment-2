package com.example.todoapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.ui.TodoDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    repository: TodoRepository,
    todoId: Int?,
    onBackClick: () -> Unit,
    viewModel: TodoDetailViewModel = viewModel(factory = TodoDetailViewModelFactory(repository))
) {
    if (todoId != null) viewModel.fetchTodoById(todoId)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Todo Detail", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                uiState.isLoading -> LoadingBox()
                uiState.error != null || uiState.todo == null ->
                    ErrorBox(uiState.error ?: "Not found")
                else -> TodoDetailContent(uiState.todo!!)
            }
        }
    }
}

/* ---------- internal helpers ---------- */

@Composable
private fun TodoDetailContent(todo: Todo) {
    // Card container with shadow, rounded corners and background color
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Title", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text(todo.title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)

            Text("Status", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (todo.completed) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (todo.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (todo.completed) "Completed" else "Pending",
                    color = if (todo.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

            Text("Identifiers", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Todo ID: ${todo.id}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("User ID: ${todo.userId}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/* ---------- reusable state boxes (shared) ---------- */

@Composable
private fun LoadingBox(modifier: Modifier = Modifier) =
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }

@Composable
private fun ErrorBox(msg: String, modifier: Modifier = Modifier) =
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Error: $msg", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
    }
