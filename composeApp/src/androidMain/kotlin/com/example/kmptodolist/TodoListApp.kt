package com.example.kmptodolist

import android.app.Application
import com.example.kmptodolist.di.androidModule
import com.example.kmptodolist.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class TodoListApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 啟動 Koin
        initKoin {
            // 提供 Android Logger
            androidLogger()
            // 提供 Android Context，這樣上面步驟中的 get() 才能正常運作
            androidContext(this@TodoListApp)
            // 載入 Android 平台的模組
            modules(androidModule)
        }
    }
}