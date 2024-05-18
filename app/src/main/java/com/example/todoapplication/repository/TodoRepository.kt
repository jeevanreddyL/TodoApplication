package com.example.todoapplication.repository

import com.example.todoapplication.data.Todo
import com.example.todoapplication.local.TodoDao
import com.example.todoapplication.network.TodoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoRepository(
    private val todoApiService: TodoApiService,
    private val todoDao: TodoDao) {
    suspend fun fetchTodos() {
        val todos = todoApiService.getTodos()
        todoDao.insertTodos(todos)
    }

    suspend fun getTodos() = withContext(Dispatchers.IO) {
        todoDao.getTodos()
    }

    suspend fun updateTodoStatus(todo: Todo) = withContext(Dispatchers.IO) {
        todoDao.updateTodo(todo)
    }
}