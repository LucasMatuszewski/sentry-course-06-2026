package com.example

import android.app.Application
import android.util.Log
import io.sentry.android.core.SentryAndroid
import io.sentry.android.core.SentryAndroidOptions

class SentryDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        Log.d("SentryDemo", "Initializing Sentry manually for Senior Dev Training...")
        
        // Manual initialization of the Sentry Android SDK
        SentryAndroid.init(this) { options: SentryAndroidOptions ->
            // 1. Set the Data Source Name (DSN) provided by the instructor
            options.dsn = "https://9e8d71703ab027cf8474da46ffc220cd@o4511643784511488.ingest.de.sentry.io/4511645677453392"
            
            // 2. Enable SDK debug logging (highly useful in Logcat during development)
            options.isDebug = true
            
            // 3. Set the environment of the application
            options.environment = "training-pl-insurance"
            
            // 4. Set a cohesive release tag
            options.release = "polsecure-insurance@1.0.0"
            
            // 5. Configure performance tracing sample rates (100% for training)
            options.tracesSampleRate = 1.0
            options.profilesSampleRate = 1.0
            
            // 6. Enable User Interaction tracking out-of-the-box
            options.isEnableUserInteractionBreadcrumbs = true
            options.isEnableUserInteractionTracing = true
            
            // 7. Enable automatic session tracking (for release health metrics)
            options.isEnableAutoSessionTracking = true
            
            // 8. Register custom global tags to segment issues on Sentry Dashboard
            options.setTag("course-id", "sentry-android-101")
            options.setTag("developer-level", "senior")
            options.setTag("target-organization", "PolSecure-Insurance-PL")
            options.setTag("host-locale", "pl_PL")
            
            // 9. Configure Sentry's logger tag
            options.setDiagnosticLevel(io.sentry.SentryLevel.DEBUG)
        }
    }
}
