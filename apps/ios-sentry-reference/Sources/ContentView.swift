// Adapted from Sentry's iOS-SwiftUI-SPM sample.
// Copyright (c) 2015 Sentry.
// Licensed under the MIT License.
// https://github.com/getsentry/sentry-cocoa/blob/1ec4cb5bf6059838f8ce137ba53aeac6765317ce/Samples/iOS-SwiftUI-SPM/Sources/ContentView.swift

import SentrySwift
import SwiftUI

private enum SampleError: Error {
    case sampleError
}

struct ContentView: View {
    var body: some View {
        VStack(spacing: 16) {
            Text("SentrySPM iOS reference")
                .font(.headline)

            Text("Configure SENTRY_DSN, then capture a handled error.")
                .multilineTextAlignment(.center)

            Button("Capture Error") {
                SentrySDK.capture(error: SampleError.sampleError)
            }
            .buttonStyle(.borderedProminent)
        }
        .padding()
        .sentryTrace("Content View")
    }
}

