package com.example.todoapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.data.Todo
import com.example.todoapplication.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository):ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    init {
        fetchTodos()
    }

    private fun fetchTodos() {
        viewModelScope.launch {
           todoRepository.fetchTodos()
            _todos.value = todoRepository.getTodos()
        }
    }

    fun updateTodoStatus(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodoStatus(todo)
            _todos.value = todoRepository.getTodos()
        }
    }
}