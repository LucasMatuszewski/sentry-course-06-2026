import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
  // Sentry Android Gradle plugin — uploads R8/ProGuard mapping + Kotlin
  // source context per release build. Active only when SENTRY_AUTH_TOKEN
  // is in the build environment, so debug builds and zero-auth CI stay
  // silent.
  alias(libs.plugins.sentry.android)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.sentrydemo.kblyzw"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      // Enable R8 obfuscation + shrinking so there is actually a mapping
      // file to upload to Sentry. Without this the release stack traces
      // are already readable and the demo loses its point. The matching
      // Sentry mapping makes deobfuscation server-side automatic.
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug {
      signingConfig = signingConfigs.getByName("debugConfig")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
}

googleServices {
  missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN
}

// Sentry Android plugin configuration.
//
// What this does at release-build time:
//   1. Generates an R8/ProGuard mapping.txt (because isMinifyEnabled=true).
//   2. Uploads mapping.txt to Sentry tagged with this app's versionName +
//      versionCode + applicationId so Sentry can deobfuscate stack
//      traces server-side.
//   3. Generates and uploads a Kotlin source bundle so issues show source
//      context (the lines of code around the failing frame).
//   4. Tracks the release in Sentry with the same identifier.
//
// All four steps require SENTRY_AUTH_TOKEN to be present in the build
// environment (org-owned token, scope org:ci). When absent (debug builds,
// fork PRs), every step is silently skipped — no upload, no error.
//
// Trainer demo path:
//   $env:SENTRY_AUTH_TOKEN="sntrys_..."   # one-time, per shell
//   ./gradlew :app:assembleRelease         # build APK with obfuscation,
//                                          # mapping + source upload run.
//
// Reference: https://docs.sentry.io/platforms/android/configuration/gradle/
sentry {
  // Default to no-op when token absent.
  ignoredBuildTypes.set(setOf("debug"))

  autoUploadProguardMapping.set(
    System.getenv("SENTRY_AUTH_TOKEN")?.isNotBlank() == true
  )
  includeProguardMapping.set(true)

  // Kotlin source context — shows code lines next to stack frames.
  includeSourceContext.set(
    System.getenv("SENTRY_AUTH_TOKEN")?.isNotBlank() == true
  )

  // Track release in Sentry; identifier matches the Sentry Android SDK
  // default release naming (applicationId@versionName+versionCode).
  autoInstallation { enabled.set(false) }   // SDK already added manually.
  tracingInstrumentation { enabled.set(true) }

  org.set("devpowers")
  projectName.set("android")
  authToken.set(System.getenv("SENTRY_AUTH_TOKEN") ?: "")
}


// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  // implementation(libs.androidx.camera.camera2)
  // implementation(libs.androidx.camera.core)
  // implementation(libs.androidx.camera.lifecycle)
  // implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  // implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  // implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  // implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.ai)
  implementation(libs.firebase.appcheck.recaptcha)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  // implementation(libs.play.services.location)
  implementation(libs.retrofit)

  // Sentry SDK & Integrations
  implementation(platform(libs.sentry.bom))
  implementation(libs.sentry.android)
  implementation(libs.sentry.compose)
  implementation(libs.sentry.android.okhttp)
  implementation(libs.sentry.android.sqlite)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}
