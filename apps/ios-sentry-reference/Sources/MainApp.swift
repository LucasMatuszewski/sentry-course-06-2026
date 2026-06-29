// Adapted from Sentry's iOS-SwiftUI-SPM sample.
// Copyright (c) 2015 Sentry.
// Licensed under the MIT License.
// https://github.com/getsentry/sentry-cocoa/blob/1ec4cb5bf6059838f8ce137ba53aeac6765317ce/Samples/iOS-SwiftUI-SPM/Sources/MainApp.swift

import SentrySwift
import SwiftUI

@main
struct MainApp: App {
    init() {
        let configuredDsn = Bundle.main.object(forInfoDictionaryKey: "SENTRY_DSN") as? String

        SentrySDK.start { options in
            options.dsn = configuredDsn?.isEmpty == false ? configuredDsn : nil
            options.debug = true
            options.tracesSampleRate = 1
            options.enableAutoPerformanceTracing = true
            options.enableUIViewControllerTracing = true
            options.enableUserInteractionTracing = true
            options.enableTimeToFullDisplayTracing = true
            options.sendDefaultPii = false
            options.attachScreenshot = false
            options.enableMetricKit = true
            // Logs are enabled for teaching. Never put PII or secrets in messages/attributes.
            options.enableLogs = true

            #if targetEnvironment(simulator)
            options.enableSpotlight = true
            #endif
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
