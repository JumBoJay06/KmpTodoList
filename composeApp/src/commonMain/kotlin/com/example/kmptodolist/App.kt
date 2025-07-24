package com.example.kmptodolist

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.example.kmptodolist.ui.screens.list.TodoListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 應用程式的進入點。
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        // 使用 Voyager 的 Navigator 來管理畫面
        Navigator(TodoListScreen())
    }
}