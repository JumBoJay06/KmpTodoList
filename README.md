# KmpTodoList

這是一個使用 Kotlin Multiplatform (KMP) 技術開發的待辦事項應用程式範例，旨在展示如何在 Android 和 iOS 之間共享程式碼，包含業務邏輯、資料儲存與 UI。

## 專案特色

* **跨平台**：一套程式碼庫可同時在 Android 和 iOS 上運行。
* **響應式 UI**：使用 Jetpack Compose for Multiplatform 建構，UI 會根據應用程式狀態的變化自動更新。
* **本地資料庫**：透過 SQLDelight 在各平台上實現本地資料庫儲存。
* **依賴注入**：利用 Koin 函式庫管理各模組間的依賴關係。
* **導航**：採用 Voyager 進行畫面的導航與管理。
* **MVVM 架構**：遵循 Model-View-ViewModel 架構模式，使程式碼結構清晰且易於維護。

## 主要功能

* **新增待辦事項**：在主畫面上方輸入文字並點擊「新增」按鈕。
* **編輯待辦事項**：點擊任一待辦事項可進入編輯頁面，修改標題與內容。
* **標示完成/未完成**：透過點擊待辦事項前的複選框來切換其完成狀態。
* **刪除待辦事項**：點擊待辦事項右側的垃圾桶圖示以刪除該項目。

## 使用的技術與套件

這個專案使用了以下主要的函式庫與技術：

| 技術 | 用途 |
| :--- | :--- |
| [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform-mobile-overview.html) | 跨平台開發框架 |
| [Jetpack Compose for Multiplatform](https://github.com/JetBrains/compose-multiplatform) | UI 框架 |
| [Voyager](https://github.com/adrielcafe/voyager) | 導航函式庫 |
| [Koin](https://insert-koin.io/) | 依賴注入框架 |
| [SQLDelight](https://github.com/cashapp/sqldelight) | 多平台資料庫函式庫 |
| [Material 3](https://m3.material.io/) | UI 元件與設計系統 |

## 專案結構

```
.
├── build.gradle.kts # 根專案的 Gradle 建構腳本，定義全域組態
├── composeApp
│   ├── build.gradle.kts # 'composeApp' 模組的 Gradle 建構腳本，定義相依性與平台目標
│   └── src
│       ├── androidMain
│       │   ├── AndroidManifest.xml # Android 應用的設定檔，定義權限、Activity 等
│       │   └── kotlin
│       │       └── com/example/kmptodolist
│       │           ├── MainActivity.kt # Android 應用的主要 Activity，是應用的進入點
│       │           ├── TodoListApp.kt # Android 應用的 Application 類別，用於初始化 Koin
│       │           ├── data/local/DatabaseDriverFactory.kt # Android 平台的 SQLDelight 資料庫驅動實作
│       │           └── di/PlatformModule.kt # Android 平台的 Koin 模組，提供平台特定的依賴
│       ├── commonMain
│       │   ├── kotlin
│       │   │   └── com/example/kmptodolist
│       │   │       ├── App.kt # 共享的 Composable 根元件，設定導航
│       │   │       ├── data/local/DatabaseDriverFactory.kt # 預期 (expect) 類別，定義了平台需提供的資料庫驅動工廠
│       │   │       ├── di/Koin.kt # 通用的 Koin 模組與初始化函式
│       │   │       └── ui/screens
│       │   │           ├── detail
│       │   │           │   ├── TodoDetailScreen.kt # 編輯待辦事項的畫面 UI
│       │   │           │   └── TodoDetailViewModel.kt # 編輯畫面的 ViewModel，處理業務邏輯
│       │   │           └── todolist
│       │   │               ├── TodoListScreen.kt # 待辦事項列表的主畫面 UI
│       │   │               └── TodoListViewModel.kt # 列表畫面的 ViewModel，處理業務邏輯
│       │   └── sqldelight/com/example/kmptodolist/db
│       │       └── Todo.sq # SQLDelight 的 SQL 檔案，定義資料庫表結構與查詢
│       ├── commonTest/kotlin/com/example/kmptodolist
│       │   └── ComposeAppCommonTest.kt # 共享模組的單元測試範例
│       └── iosMain
│           └── kotlin
│               └── com/example/kmptodolist
│                   ├── MainViewController.kt # iOS UI 的進入點，用於橋接 SwiftUI 與 Compose
│                   ├── data/local/DatabaseDriverFactory.kt # iOS 平台的 SQLDelight 資料庫驅動實作
│                   └── di/KoinIOS.kt # iOS 平台的 Koin 初始化與平台模組
├── gradle
│   ├── libs.versions.toml # Gradle 的版本目錄，集中管理專案的依賴版本
│   └── wrapper
│       └── gradle-wrapper.properties # Gradle Wrapper 的設定檔，確保建構環境一致
├── gradlew # Unix/macOS 系統的 Gradle Wrapper 執行腳本
├── gradlew.bat # Windows 系統的 Gradle Wrapper 執行腳本
├── iosApp
│   ├── Configuration/Config.xcconfig # Xcode 的組態設定檔，用於管理建構變數
│   ├── iosApp
│   │   ├── ContentView.swift # SwiftUI 視圖，用於承載 Compose 內容
│   │   ├── Info.plist # iOS 應用的資訊屬性列表，如 bundle identifier 等
│   │   └── iOSApp.swift # iOS 應用的主要進入點 (App struct)
│   ├── iosApp.xcodeproj
│   │   ├── project.pbxproj # Xcode 專案檔，定義專案結構、目標和建構設定
│   │   └── project.xcworkspace
│   │       └── contents.xcworkspacedata # Xcode 工作區設定檔
└── settings.gradle.kts # Gradle 設定腳本，定義專案中包含的模組
```