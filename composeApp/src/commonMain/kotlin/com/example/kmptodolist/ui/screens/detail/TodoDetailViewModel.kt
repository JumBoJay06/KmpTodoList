package com.example.kmptodolist.ui.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.kmptodolist.db.Todo
import com.example.kmptodolist.db.TodoQueries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TodoDetailUiState(
    val todo: Todo? = null,
    val editedTitle: String = "",
    val editedContent: String = "" // 新增 content 的狀態
)

class TodoDetailViewModel(
    private val todoId: Long,
    private val todoQueries: TodoQueries
) : ScreenModel {

    private val _uiState = MutableStateFlow(TodoDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTodo()
    }

    private fun loadTodo() {
        screenModelScope.launch {
            val todo = todoQueries.selectById(todoId).executeAsOneOrNull()
            _uiState.update {
                it.copy(
                    todo = todo,
                    editedTitle = todo?.title ?: "",
                    editedContent = todo?.content ?: "" // 讀取 content，處理 null 的情況
                )
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(editedTitle = newTitle) }
    }

    // 新增 content 的處理函式
    fun onContentChange(newContent: String) {
        _uiState.update { it.copy(editedContent = newContent) }
    }

    fun saveTodo() {
        screenModelScope.launch {
            val state = _uiState.value
            if (state.editedTitle.isNotBlank()) {
                todoQueries.updateTodo(
                    title = state.editedTitle,
                    content = state.editedContent,
                    id = todoId
                )
            }
        }
    }
}
