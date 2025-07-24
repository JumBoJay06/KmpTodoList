package com.example.kmptodolist.data.local

import app.cash.sqldelight.db.SqlDriver

/**
 * 預期 (expect) 的資料庫驅動程式工廠。
 * 這是一個在 commonMain 中宣告的介面，
 * 實際的實作 (actual) 會在各個平台 (Android, iOS) 中提供。
 */
expect class DatabaseDriverFactory {
    /**
     * 建立並回傳一個 SqlDriver 實例。
     */
    fun createDriver(): SqlDriver
}