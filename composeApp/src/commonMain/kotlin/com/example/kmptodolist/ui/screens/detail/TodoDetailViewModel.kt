package com.example.kmptodolist.ui.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.kmptodolist.db.TodoQueries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * TodoDetailViewModel 負責處理待辦事項詳細資訊的業務邏輯。
 * @param todoId 待辦事項的 ID。
 * @param todoQueries 用於存取待辦事項資料庫的查詢介面。
 */
class TodoDetailViewModel(
    private val todoId: Long,
    private val todoQueries: TodoQueries
) : ScreenModel {

    // 用於儲存編輯後的標題的 MutableStateFlow
    private val _editedTitle = MutableStateFlow("")
    // 將 editedTitle 公開為 StateFlow，讓 UI 可以觀察其變化
    val editedTitle = _editedTitle.asStateFlow()
    // 用於儲存編輯後的內容的 MutableStateFlow
    private val _editedContent = MutableStateFlow("")
    // 將 editedContent 公開為 StateFlow，讓 UI 可以觀察其變化
    val editedContent = _editedContent.asStateFlow()

    init {
        loadTodo()
    }

    /**
     * 載入待辦事項的詳細資訊。
     */
    private fun loadTodo() {
        screenModelScope.launch {
            val todo = todoQueries.selectById(todoId).executeAsOneOrNull()
            _editedTitle.update { todo?.title ?: "" }
            _editedContent.update { todo?.content ?: "" } // 初始化 content 狀態
        }
    }

    /**
     * 當標題發生變化時呼叫。
     * @param newTitle 新的標題內容。
     */
    fun onTitleChange(newTitle: String) {
        _editedTitle.update { newTitle }
    }

    /**
     * 當內容發生變化時呼叫。
     * @param newContent 新的內容。
     */
    fun onContentChange(newContent: String) {
        _editedContent.update { newContent }
    }

    /**
     * 儲存待辦事項的變更。
     */
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
