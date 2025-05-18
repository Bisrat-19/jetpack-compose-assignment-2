package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.data.local.DatabaseProvider
import com.example.todoapp.data.network.RetrofitInstance
import com.example.todoapp.ui.detail.TodoDetailScreen
import com.example.todoapp.ui.list.TodoListScreen
import com.example.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = TodoRepository(
            apiService = RetrofitInstance.api,
            todoDao = DatabaseProvider.getDatabase(this).todoDao()
        )
        setContent {
            TodoAppTheme {
                TodoApp(repository)
            }
        }
    }
}

@Composable
fun TodoApp(repository: TodoRepository) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "todo_list") {
        composable("todo_list") {
            TodoListScreen(
                repository = repository,
                onTodoClick = { todoId ->
                    navController.navigate("todo_detail/$todoId")
                }
            )
        }
        composable("todo_detail/{todoId}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")?.toIntOrNull()
            TodoDetailScreen(
                repository = repository,
                todoId = todoId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}