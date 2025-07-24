package com.example.kmptodolist.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.kmptodolist.db.AppDatabase

/**
 * iOS 平台的資料庫驅動程式工廠實際 (actual) 實作。
 */
actual class DatabaseDriverFactory {
    /**
     * 建立並回傳一個 iOS 平台的 SqlDriver 實例。
     * @return NativeSqliteDriver，用於在 iOS 上存取資料庫。
     */
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "todo.db")
    }
}