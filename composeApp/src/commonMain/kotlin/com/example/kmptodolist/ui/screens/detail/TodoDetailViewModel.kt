package com.example.kmptodolist.ui.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.kmptodolist.db.TodoQueries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoDetailViewModel(
    private val todoId: Long,
    private val todoQueries: TodoQueries
) : ScreenModel {

    private val _editedTitle = MutableStateFlow("")
    val editedTitle = _editedTitle.asStateFlow()
    private val _editedContent = MutableStateFlow("")
    val editedContent = _editedContent.asStateFlow()

    init {
        loadTodo()
    }

    private fun loadTodo() {
        screenModelScope.launch {
            val todo = todoQueries.selectById(todoId).executeAsOneOrNull()
            _editedTitle.update { todo?.title ?: "" }
            _editedContent.update { todo?.content ?: "" } // 初始化 content 狀態
        }
    }

    fun onTitleChange(newTitle: String) {
        _editedTitle.update { newTitle }
    }

    // 新增 content 的處理函式
    fun onContentChange(newContent: String) {
        _editedContent.update { newContent }
    }

    fun saveTodo() {
        screenModelScope.launch {
            val title = _editedTitle.value
            if (title.isNotBlank()) {
                todoQueries.updateTodo(
                    title = _editedTitle.value,
                    content = _editedContent.value,
                    id = todoId
                )
            }
        }
    }
}
