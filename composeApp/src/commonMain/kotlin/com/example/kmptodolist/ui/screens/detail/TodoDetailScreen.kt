package com.example.kmptodolist.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kmptodolist.composeapp.generated.resources.Res
import kmptodolist.composeapp.generated.resources.back_button_description
import kmptodolist.composeapp.generated.resources.confirm_button
import kmptodolist.composeapp.generated.resources.content_placeholder
import kmptodolist.composeapp.generated.resources.edit_todo_title
import kmptodolist.composeapp.generated.resources.save_button
import kmptodolist.composeapp.generated.resources.title_placeholder
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

/**
 * 待辦事項詳細資訊畫面。
 * @param todoId 待辦事項的 ID。
 */
class TodoDetailScreen(private val todoId: Long) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // 取得 Voyager 導航器
        val navigator = LocalNavigator.currentOrThrow
        // 使用 Koin 傳遞 todoId 給 ViewModel
        val viewModel = koinScreenModel<TodoDetailViewModel> {
            parametersOf(todoId)
        }
        // 觀察編輯後的標題的變化
        val editedTitle by viewModel.editedTitle.collectAsState()
        // 觀察編輯後的內容的變化
        val editedContent by viewModel.editedContent.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.edit_todo_title)) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.back_button_description)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (editedTitle.isEmpty()) {
                // 如果標題為空，顯示載入中
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // 標題輸入框
                    OutlinedTextField(
                        value = editedTitle,
                        onValueChange = viewModel::onTitleChange,
                        label = { Text(stringResource(Res.string.title_placeholder)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 內容輸入框
                    OutlinedTextField(
                        value = editedContent,
                        onValueChange = viewModel::onContentChange,
                        label = { Text(stringResource(Res.string.content_placeholder)) },
                        modifier = Modifier.fillMaxWidth().weight(1f) // 讓它填滿剩餘空間
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    // 儲存按鈕
                    Button(
                        onClick = {
                            viewModel.saveTodo()
                            navigator.pop()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(Res.string.save_button))
                    }
                }
            }
        }
    }
}
