package com.example.kmptodolist.ui.screens.todolist

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.kmptodolist.db.Todo
import com.example.kmptodolist.db.TodoQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val todoQueries: TodoQueries
) : ScreenModel {
    private val _newTodoText = MutableStateFlow("")
    val newTodoText = _newTodoText.asStateFlow()
    val todos: StateFlow<List<Todo>> =
        todoQueries.selectAll().asFlow().mapToList(Dispatchers.IO).stateIn(
            scope = screenModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onNewTodoTextChange(text: String) {
        _newTodoText.update { text }
    }

    fun addTodo() {
        if (_newTodoText.value.isBlank()) return
        screenModelScope.launch {
            todoQueries.insert(_newTodoText.value)
            _newTodoText.update { "" }
        }
    }

    fun toggleTodoCompletion(id: Long, isCompleted: Boolean) {
        screenModelScope.launch {
            todoQueries.updateCompletion(isCompleted, id)
        }
    }

    fun deleteTodo(id: Long) {
        screenModelScope.launch {
            todoQueries.deleteById(id)
        }
    }
}
