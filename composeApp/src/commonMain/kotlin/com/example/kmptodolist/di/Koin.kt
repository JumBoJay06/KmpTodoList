package com.example.kmptodolist.di

import com.example.kmptodolist.data.local.DatabaseDriverFactory
import com.example.kmptodolist.db.AppDatabase
import com.example.kmptodolist.ui.screens.detail.TodoDetailViewModel
import com.example.kmptodolist.ui.screens.list.TodoListViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * 這是通用的 Koin 模組，只包含所有平台共享的依賴。
 */
val commonModule = module {
    // DatabaseDriverFactory 是平台相關的，它將由每個平台的特定模組提供。

    // 提供 AppDatabase 的單例實例
    single { AppDatabase(get<DatabaseDriverFactory>().createDriver()) }
    // 提供 AppDatabase.todoQueries 的單例實例
    single { get<AppDatabase>().todoQueries }

    // 提供 TodoListViewModel 的工廠實例
    factory { TodoListViewModel(get()) }
    // 提供 TodoDetailViewModel 的工廠實例，並傳入 todoId
    factory { (todoId: Long) -> TodoDetailViewModel(todoId, get()) }
}

/**
 * 初始化 Koin，使其接受一個配置區塊 (lambda)。
 * 這讓我們可以在各個平台（例如 Android）啟動 Koin 時，傳入特定的配置。
 * @param appDeclaration 用於平台特定配置的 lambda。
 */
fun initKoin(appDeclaration: KoinApplication.() -> Unit) {
    startKoin {
        appDeclaration()
        modules(commonModule)
    }
}