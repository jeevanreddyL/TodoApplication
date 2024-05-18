package com.example.todoapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapplication.data.Todo
import com.example.todoapplication.local.AppDatabase
import com.example.todoapplication.network.RetrofitInstance
import com.example.todoapplication.repository.TodoRepository
import com.example.todoapplication.ui.theme.TodoApplicationTheme
import com.example.todoapplication.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    private lateinit var todoViewModel: TodoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val todoApiService = RetrofitInstance.api
        val todoRepository = TodoRepository(
            todoApiService = todoApiService,
            todoDao = AppDatabase.getDatabase(context =  applicationContext).todoDao()
        )
        todoViewModel =  TodoViewModel(todoRepository = todoRepository)
        setContent {
            TodoApplicationTheme {
                TodoListScreen(todoViewModel = todoViewModel)
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onTodoStatusChanged: (Todo) -> Unit) {
    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Checkbox(
                checked = todo.completed,
                onCheckedChange = { isChecked ->
                    val updatedTodo = todo.copy(completed = isChecked)
                    onTodoStatusChanged(updatedTodo)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(todo.title)
        }
    }
}

@Composable
fun TodoListScreen(todoViewModel: TodoViewModel) {
    val todos by todoViewModel.todos.collectAsState()
    val (pendingTodos, completedTodos) = todos.partition { !it.completed }
    LazyColumn {
        item {
            Text("Pending", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(8.dp))
        }
        items(pendingTodos) { todo ->
            TodoItem(todo = todo, onTodoStatusChanged = { updatedTodo ->
                todoViewModel.updateTodoStatus(updatedTodo)
            })
        }
        item {
            Text("Completed", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(8.dp))
        }
        items(completedTodos) { todo ->
            TodoItem(todo = todo, onTodoStatusChanged = { updatedTodo ->
                todoViewModel.updateTodoStatus(updatedTodo)
            })
        }

    }
}