package com.example.kmptodolist.ui.screens.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.kmptodolist.db.Todo
import com.example.kmptodolist.ui.screens.detail.TodoDetailScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

object TodoListScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<TodoListViewModel>()
        val newTodoText by viewModel.newTodoText.collectAsState()
        val todos by viewModel.todos.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        var showDeleteDialog by remember { mutableStateOf<Long?>(null) } // 狀態：要刪除的

        // 刪除確認對話框
        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("確認刪除") },
                text = { Text("你確定要刪除這個待辦事項嗎？") },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog?.let { viewModel.deleteTodo(it) }
                            showDeleteDialog = null
                        }
                    ) {
                        Text("確認")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = null }) {
                        Text("取消")
                    }
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("待辦事項列表") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // 輸入框和按鈕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTodoText,
                        onValueChange = viewModel::onNewTodoTextChange,
                        label = { Text("新的待辦事項") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = viewModel::addTodo) {
                        Text("新增")
                    }
                }

                // 分隔線
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                if (todos.isEmpty()) {
                    // 如果沒有待辦事項，顯示提示
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("目前沒有待辦事項", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    // 待辦事項列表
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(todos, key = { it.id }) { todo -> // 使用 key 提升性能
                            TodoItemRow(
                                todo = todo,
                                onToggle = viewModel::toggleTodoCompletion,
                                onDelete = { id ->
                                    showDeleteDialog = id // 點擊刪除時，設置要刪除的
                                },
                                onClick = {
                                    navigator.push(TodoDetailScreen(todo.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItemRow(
    todo: Todo,
    onToggle: (id: Long, isCompleted: Boolean) -> Unit,
    onDelete: (id: Long) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = if (todo.is_completed) Color.LightGray else Color.White
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.is_completed,
                onCheckedChange = { onToggle(todo.id, it) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = if (todo.is_completed) {
                        MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.LineThrough)
                    } else {
                        MaterialTheme.typography.bodyLarge
                    }
                )
                if (todo.content != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = todo.content, // 處理可能為 null 的 content
                        style = if (todo.is_completed) {
                            MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
                        } else {
                            MaterialTheme.typography.bodyMedium
                        }
                    )
                }

            }
            // 至左的刪除按鈕
            IconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = { onDelete(todo.id) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = Color.Red,
                    contentDescription = "刪除待辦事項"
                )
            }
        }
    }
}

