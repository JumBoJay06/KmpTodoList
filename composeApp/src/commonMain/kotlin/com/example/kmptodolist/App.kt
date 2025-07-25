package com.example.kmptodolist

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.example.kmptodolist.ui.screens.list.TodoListScreen
import com.example.kmptodolist.ui.theme.KmpTodoListTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 應用程式的進入點。
 */
@Composable
@Preview
fun App() {
    KmpTodoListTheme {
        // 使用 Voyager 的 Navigator 來管理畫面
        Navigator(TodoListScreen())
    }
}