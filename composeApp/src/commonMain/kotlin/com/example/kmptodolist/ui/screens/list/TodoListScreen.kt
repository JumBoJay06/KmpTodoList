package com.example.kmptodolist.ui.screens.list

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
import kmptodolist.composeapp.generated.resources.Res
import kmptodolist.composeapp.generated.resources.add_button
import kmptodolist.composeapp.generated.resources.cancel_button
import kmptodolist.composeapp.generated.resources.confirm_button
import kmptodolist.composeapp.generated.resources.delete_dialog_text
import kmptodolist.composeapp.generated.resources.delete_dialog_title
import kmptodolist.composeapp.generated.resources.delete_todo_description
import kmptodolist.composeapp.generated.resources.new_todo_placeholder
import kmptodolist.composeapp.generated.resources.no_todos_text
import kmptodolist.composeapp.generated.resources.todo_list_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 待辦事項列表畫面。
 */
class TodoListScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    override fun Content() {
        // 取得 ViewModel
        val viewModel = koinScreenModel<TodoListViewModel>()
        // 觀察新待辦事項文字的變化
        val newTodoText by viewModel.newTodoText.collectAsState()
        // 觀察待辦事項列表的變化
        val todos by viewModel.todos.collectAsState()
        // 取得 Voyager 導航器
        val navigator = LocalNavigator.currentOrThrow
        // 狀態：要刪除的 id
        var showDeleteDialog by remember { mutableStateOf<Long?>(null) }

        // 刪除確認對話框
        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text(stringResource(Res.string.delete_dialog_title)) },
                text = { Text(stringResource(Res.string.delete_dialog_text)) },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog?.let { viewModel.deleteTodo(it) }
                            showDeleteDialog = null
                        }
                    ) {
                        Text(stringResource(Res.string.confirm_button))
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = null }) {
                        Text(stringResource(Res.string.cancel_button))
                    }
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text(stringResource(Res.string.todo_list_title)) })
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
                        label = { Text(stringResource(Res.string.new_todo_placeholder)) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = viewModel::addTodo) {
                        Text(stringResource(Res.string.add_button))
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
                        Text(stringResource(Res.string.no_todos_text), style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    // 待辦事項列表
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(todos, key = { it.id }) { todo -> // 使用 key 提升性能
                            TodoItemRow(
                                todo = todo,
                                onToggle = viewModel::toggleTodoCompletion,
                                onDelete = { id ->
                                    showDeleteDialog = id // 點擊刪除時，設置要刪除的 id
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

/**
 * 待辦事項列表中的單一項目。
 * @param todo 待辦事項。
 * @param onToggle 切換完成狀態的回呼。
 * @param onDelete 刪除的回呼。
 * @param onClick 點擊的回呼。
 */
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
                // 處理可能為 null 的 content
                if (todo.content != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = todo.content,
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
                    contentDescription = stringResource(Res.string.delete_todo_description)
                )
            }
        }
    }
}

