package com.example.kmptodolist.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.kmptodolist.db.AppDatabase

/**
 * Android 平台的資料庫驅動程式工廠實際 (actual) 實作。
 * @param context Android 應用程式的上下文。
 */
actual class DatabaseDriverFactory(private val context: Context) {
    /**
     * 建立並回傳一個 Android 平台的 SqlDriver 實例。
     * @return AndroidSqliteDriver，用於在 Android 上存取資料庫。
     */
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "todo.db")
    }
}