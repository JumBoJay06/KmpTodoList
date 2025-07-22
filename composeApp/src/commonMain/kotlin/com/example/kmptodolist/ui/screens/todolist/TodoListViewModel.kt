package com.example.kmptodolist.ui.screens.todolist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.kmptodolist.db.Todo
import com.example.kmptodolist.db.TodoQueries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TodoListUiState(
    val todos: List<Todo> = emptyList(),
    val newTodoText: String = ""
)

class TodoListViewModel(
    private val todoQueries: TodoQueries
) : ScreenModel {

    private val _uiState = MutableStateFlow(TodoListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    fun loadTodos() {
        screenModelScope.launch {
            val todos = todoQueries.selectAll().executeAsList()
            _uiState.update { it.copy(todos = todos) }
        }
    }

    fun onNewTodoTextChange(text: String) {
        _uiState.update { it.copy(newTodoText = text) }
    }

    fun addTodo() {
        if (_uiState.value.newTodoText.isBlank()) return
        screenModelScope.launch {
            todoQueries.insert(_uiState.value.newTodoText)
            _uiState.update { it.copy(newTodoText = "") }
            loadTodos()
        }
    }

    fun toggleTodoCompletion(id: Long, isCompleted: Boolean) {
        screenModelScope.launch {
            todoQueries.updateCompletion(if (isCompleted) 1 else 0, id)
            loadTodos()
        }
    }

    fun deleteTodo(id: Long) {
        screenModelScope.launch {
            todoQueries.deleteById(id)
            loadTodos()
        }
    }
}
