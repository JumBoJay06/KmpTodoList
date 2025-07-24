import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    //  在 App 的初始化方法中啟動 Koin
    init() {
        KoinIOSKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}