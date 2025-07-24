package com.example.kmptodolist.di

import com.example.kmptodolist.data.local.DatabaseDriverFactory
import org.koin.dsl.module

/**
 * iOS 平台的 Koin 初始化函式。
 * 會在 iosApp.swift 中被呼叫。
 */
fun initKoinIos() = initKoin {
    modules(iosModule)
}

/**
 * iOS 平台的 Koin 模組。
 */
val iosModule = module {
    // 提供 DatabaseDriverFactory 的單例實例
    single { DatabaseDriverFactory() }
}