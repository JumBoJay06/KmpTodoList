package com.example.kmptodolist.di

import com.example.kmptodolist.data.local.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * 這是 Android 平台的特定模組。
 */
val androidModule: Module = module {
    // 在這裡我們提供 DatabaseDriverFactory 的具體實作。
    // Android 的 SQLDelight driver 需要 Context，所以我們使用 get() 來從 Koin 取得它。
    single { DatabaseDriverFactory(get()) }
}