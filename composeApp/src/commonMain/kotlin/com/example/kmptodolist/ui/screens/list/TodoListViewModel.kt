package com.example.kmptodolist.ui.screens.list

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

/**
 * TodoListViewModel 負責處理待辦事項列表的業務邏輯。
 * @param todoQueries 用於存取待辦事項資料庫的查詢介面。
 */
class TodoListViewModel(
    private val todoQueries: TodoQueries
) : ScreenModel {
    // 用於儲存新待辦事項標題的 MutableStateFlow
    private val _newTodoText = MutableStateFlow("")
    // 將 newTodoText 公開為 StateFlow，讓 UI 可以觀察其變化
    val newTodoText = _newTodoText.asStateFlow()
    // 從資料庫查詢所有待辦事項，並將其轉換為 StateFlow
    val todos: StateFlow<List<Todo>> =
        todoQueries.selectAll().asFlow().mapToList(Dispatchers.IO).stateIn(
            scope = screenModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * 當新待辦事項的文字發生變化時呼叫。
     * @param text 新的文字內容。
     */
    fun onNewTodoTextChange(text: String) {
        _newTodoText.update { text }
    }

    /**
     * 新增一個待辦事項。
     */
    fun addTodo() {
        if (_newTodoText.value.isBlank()) return
        screenModelScope.launch {
            todoQueries.insert(_newTodoText.value)
            _newTodoText.update { "" }
        }
    }

    /**
     * 切換待辦事項的完成狀態。
     * @param id 待辦事項的 ID。
     * @param isCompleted 是否完成。
     */
    fun toggleTodoCompletion(id: Long, isCompleted: Boolean) {
        screenModelScope.launch {
            todoQueries.updateCompletion(isCompleted, id)
        }
    }

    /**
     * 刪除一個待辦事項。
     * @param id 待辦事項的 ID。
     */
    fun deleteTodo(id: Long) {
        screenModelScope.launch {
            todoQueries.deleteById(id)
        }
    }
}
