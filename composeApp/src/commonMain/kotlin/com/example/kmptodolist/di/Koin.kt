package com.example.kmptodolist.di

import com.example.kmptodolist.data.local.DatabaseDriverFactory
import com.example.kmptodolist.db.AppDatabase
import com.example.kmptodolist.ui.screens.detail.TodoDetailViewModel
import com.example.kmptodolist.ui.screens.todolist.TodoListViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * 這是通用的 Koin 模組，只包含所有平台共享的依賴。
 * 為了清楚起見，我將它從 appModule 更名為 commonModule。
 */
val commonModule = module {
    // DatabaseDriverFactory 是平台相關的，所以我們從這裡移除。
    // 它將由每個平台的特定模組提供。

    // AppDatabase 依賴於 DatabaseDriverFactory，Koin 會從平台模組中找到它。
    single { AppDatabase(get<DatabaseDriverFactory>().createDriver()) }
    single { get<AppDatabase>().todoQueries }

    factory { TodoListViewModel(get()) }
    factory { (todoId: Long) -> TodoDetailViewModel(todoId, get()) }
}

/**
 * 更新 initKoin 函數，使其接受一個配置區塊 (lambda)。
 * 這讓我們可以在各個平台（例如 Android）啟動 Koin 時，傳入特定的配置。
 */
fun initKoin(appDeclaration: KoinApplication.() -> Unit) {
    startKoin {
        appDeclaration()
        modules(commonModule)
    }
}