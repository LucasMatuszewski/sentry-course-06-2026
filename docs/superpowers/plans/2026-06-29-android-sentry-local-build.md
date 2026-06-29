# Android Sentry Local Build Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make `apps/android` build reproducibly on Windows and produce a debug-signed, R8-minified APK with Sentry ProGuard and source-context metadata.

**Architecture:** Keep AGP 9.1.1 and pin its documented Gradle 9.3.1 wrapper. Upgrade the Sentry plugin and SDK, add a local-only `demoRelease` build type, use one BuildConfig-driven manual SDK initialization, and verify generated artifacts before attempting USB runtime verification.

**Tech Stack:** Gradle 9.3.1, Android Gradle Plugin 9.1.1, Kotlin 2.2.10, Sentry Android SDK 8.46.0, Sentry Android Gradle plugin 6.13.0, JUnit/Robolectric, PowerShell, ADB.

---

## File structure

- `apps/android/gradlew`, `apps/android/gradlew.bat`, `apps/android/gradle/wrapper/*`: reproducible Gradle entry point.
- `apps/android/gradle/libs.versions.toml`: compatible Sentry plugin and SDK versions.
- `apps/android/app/build.gradle.kts`: local build variants and Sentry artifact pipeline.
- `apps/android/.env.example`: public runtime Sentry defaults only.
- `apps/android/app/src/main/AndroidManifest.xml`: disable automatic SDK initialization.
- `apps/android/app/src/main/java/com/example/SentryDemoApplication.kt`: the single manual initialization path.
- `apps/android/app/src/main/java/com/example/SentryDemoViewModel.kt`: safe synthetic event context.
- `apps/android/app/src/test/java/com/example/SentryTrainingConfigTest.kt`: runtime configuration behavior.
- `.github/workflows/ci.yml`: wrapper-based Android build verification.
- `course-materials/guides/source-maps-demo.md`: commands and expected artifact/task names.

### Task 1: Repair the Gradle and Sentry toolchain

**Files:**
- Modify: `apps/android/gradle/libs.versions.toml`
- Create: `apps/android/gradlew`
- Create: `apps/android/gradlew.bat`
- Create: `apps/android/gradle/wrapper/gradle-wrapper.jar`
- Create: `apps/android/gradle/wrapper/gradle-wrapper.properties`

- [ ] **Step 1: Reproduce the current configuration failure**

Run:

```powershell
cd apps\android
& "C:\Users\BiuroEdukey\.gradle\wrapper\dists\gradle-9.5.1-bin\iq79hdu3mqx29lgffhp8bfmx\gradle-9.5.1\bin\gradle.bat" :app:tasks --no-daemon
```

Expected: failure applying Sentry Gradle plugin `5.5.0` because AGP 9 no longer exposes `AppExtension`.

- [ ] **Step 2: Update only the Sentry versions**

Set:

```toml
sentry = "8.46.0"
sentryGradlePlugin = "6.13.0"
```

- [ ] **Step 3: Verify Gradle configuration**

Run the same cached Gradle command.

Expected: `BUILD SUCCESSFUL` and Android/Sentry tasks listed.

- [ ] **Step 4: Generate the pinned wrapper**

Run:

```powershell
& "C:\Users\BiuroEdukey\.gradle\wrapper\dists\gradle-9.5.1-bin\iq79hdu3mqx29lgffhp8bfmx\gradle-9.5.1\bin\gradle.bat" wrapper --gradle-version 9.3.1 --distribution-type bin
.\gradlew.bat --version
```

Expected: Gradle `9.3.1`.

- [ ] **Step 5: Commit**

```powershell
git add apps/android/gradle/libs.versions.toml apps/android/gradlew apps/android/gradlew.bat apps/android/gradle/wrapper
git commit -m "build(android): repair Gradle and Sentry toolchain"
```

### Task 2: Add the locally installable minified variant

**Files:**
- Modify: `apps/android/app/build.gradle.kts`

- [ ] **Step 1: Verify the current release signing failure**

Run:

```powershell
cd apps\android
.\gradlew.bat :app:assembleRelease --no-daemon
```

Expected: failure because the configured private release and project debug keystores do not exist.

- [ ] **Step 2: Replace custom signing with Android debug signing**

Remove both custom `signingConfigs` declarations. Keep `release` minified and unsigned. Configure:

```kotlin
buildTypes {
  release {
    isCrunchPngs = false
    isMinifyEnabled = true
    isShrinkResources = true
    proguardFiles(
      getDefaultProguardFile("proguard-android-optimize.txt"),
      "proguard-rules.pro",
    )
  }
  debug {
    applicationIdSuffix = ".debug"
    versionNameSuffix = "-debug"
  }
  create("demoRelease") {
    initWith(getByName("release"))
    signingConfig = signingConfigs.getByName("debug")
    matchingFallbacks += listOf("release")
    versionNameSuffix = "-demo"
  }
}
```

- [ ] **Step 3: Make source-context generation independent of upload credentials**

Configure the plugin so local artifacts are always produced:

```kotlin
val sentryAuthToken = providers.environmentVariable("SENTRY_AUTH_TOKEN")
val hasSentryAuthToken = sentryAuthToken.map { it.isNotBlank() }.orElse(false)

sentry {
  ignoredBuildTypes.set(setOf("debug"))
  includeProguardMapping.set(true)
  autoUploadProguardMapping.set(hasSentryAuthToken)
  includeSourceContext.set(true)
  autoUploadSourceContext.set(hasSentryAuthToken)
  sentryAuthToken.orNull?.let { authToken.set(it) }
}
```

Retain the existing organization and project slugs.

- [ ] **Step 4: Build both local variants**

Run:

```powershell
.\gradlew.bat :app:assembleDebug :app:assembleDemoRelease --no-daemon
```

Expected: both tasks succeed without `SENTRY_AUTH_TOKEN`.

- [ ] **Step 5: Verify R8 and Sentry artifacts**

Run:

```powershell
Test-Path .\app\build\outputs\apk\debug\app-debug.apk
Test-Path .\app\build\outputs\apk\demoRelease\app-demoRelease.apk
Test-Path .\app\build\outputs\mapping\demoRelease\mapping.txt
Get-ChildItem -Recurse .\app\build\intermediates\sentry\demoRelease
```

Expected: each `Test-Path` prints `True`, and the Sentry tree contains a source-bundle ZIP.

- [ ] **Step 6: Commit**

```powershell
git add apps/android/app/build.gradle.kts
git commit -m "build(android): add minified local demo variant"
```

### Task 3: Unify Sentry runtime initialization

**Files:**
- Modify: `apps/android/.env.example`
- Modify: `apps/android/app/src/main/AndroidManifest.xml`
- Modify: `apps/android/app/src/main/java/com/example/SentryDemoApplication.kt`
- Create: `apps/android/app/src/main/java/com/example/SentryTrainingConfig.kt`
- Create: `apps/android/app/src/test/java/com/example/SentryTrainingConfigTest.kt`

- [ ] **Step 1: Write the failing configuration tests**

Create tests covering:

```kotlin
class SentryTrainingConfigTest {
  @Test
  fun `uses configured DSN and environment`() {
    val config = SentryTrainingConfig.from(
      dsn = "https://public@example.invalid/1",
      environment = "training-local",
    )
    assertEquals("https://public@example.invalid/1", config.dsn)
    assertEquals("training-local", config.environment)
  }

  @Test
  fun `rejects blank DSN`() {
    assertFailsWith<IllegalArgumentException> {
      SentryTrainingConfig.from(dsn = " ", environment = "training-local")
    }
  }
}
```

- [ ] **Step 2: Run the tests and verify RED**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests com.example.SentryTrainingConfigTest
```

Expected: compilation failure because `SentryTrainingConfig` does not exist.

- [ ] **Step 3: Implement the minimal configuration object**

Implement an immutable configuration with `from(dsn, environment)` that trims both values, rejects a blank DSN, and defaults a blank environment to `training-local`.

- [ ] **Step 4: Run the tests and verify GREEN**

Run the same targeted test command.

Expected: all `SentryTrainingConfigTest` tests pass.

- [ ] **Step 5: Configure BuildConfig values**

Add public defaults to `.env.example`:

```properties
SENTRY_DSN=https://PUBLIC_KEY@PUBLIC_HOST/PUBLIC_PROJECT_ID
SENTRY_ENVIRONMENT=training-local
```

Use the existing public Android project DSN as the repository default, not an auth token.

- [ ] **Step 6: Disable manifest auto-init**

Remove duplicated DSN, trace, and profile metadata. Add:

```xml
<meta-data
    android:name="io.sentry.auto-init"
    android:value="false" />
```

- [ ] **Step 7: Use the single manual initialization path**

Build `SentryTrainingConfig` from `BuildConfig.SENTRY_DSN` and `BuildConfig.SENTRY_ENVIRONMENT`. Set DSN, environment, `isSendDefaultPii = false`, training sample rates, and user-interaction tracing. Remove the hard-coded release so the SDK computes it from the installed package and build version.

- [ ] **Step 8: Run unit tests and assemble both variants**

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDemoRelease --no-daemon
```

Expected: success.

- [ ] **Step 9: Commit**

```powershell
git add apps/android/.env.example apps/android/app/src/main/AndroidManifest.xml apps/android/app/src/main/java/com/example/SentryDemoApplication.kt apps/android/app/src/main/java/com/example/SentryTrainingConfig.kt apps/android/app/src/test/java/com/example/SentryTrainingConfigTest.kt
git commit -m "fix(android): unify Sentry runtime configuration"
```

### Task 4: Remove sensitive synthetic identifiers from events

**Files:**
- Modify: `apps/android/app/src/main/java/com/example/SentryDemoViewModel.kt`
- Modify: `apps/android/app/src/test/java/com/example/ExampleUnitTest.kt`

- [ ] **Step 1: Add a failing source-safety assertion**

Add a test that reads the ViewModel source and rejects the keys `pesel_input`, `pesel_checked`, and `"pesel" to peselNumber`.

- [ ] **Step 2: Run the test and verify RED**

Expected: failure identifying the current raw identifier fields.

- [ ] **Step 3: Replace raw values with safe diagnostics**

Keep only booleans, input length, synthetic validation category, and claim type. Do not put the entered identifier in breadcrumbs, exception messages, tags, or contexts.

- [ ] **Step 4: Run all unit tests**

```powershell
.\gradlew.bat :app:testDebugUnitTest --no-daemon
```

Expected: success.

- [ ] **Step 5: Commit**

```powershell
git add apps/android/app/src/main/java/com/example/SentryDemoViewModel.kt apps/android/app/src/test/java/com/example/ExampleUnitTest.kt
git commit -m "fix(android): keep Sentry demo context pseudonymous"
```

### Task 5: Make CI and course instructions execute the real build

**Files:**
- Modify: `.github/workflows/ci.yml`
- Modify: `apps/android/README.md`
- Modify: `course-materials/guides/source-maps-demo.md`
- Modify: `course-materials/guides/setup-android.md`

- [ ] **Step 1: Replace Android XML-only CI**

Install Java 17, run `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleDemoRelease`, and assert APK, mapping, debug metadata, and source-bundle paths without setting an auth token.

- [ ] **Step 2: Correct trainer commands**

Document:

```powershell
$env:SENTRY_AUTH_TOKEN = "sntrys_REPLACE_WITH_ORG_TOKEN"
.\gradlew.bat :app:assembleDemoRelease
Remove-Item Env:SENTRY_AUTH_TOKEN
```

Use the real plugin task name `uploadSentryProguardMappingsDemoRelease` and the current manual command `sentry-cli proguard upload`.

- [ ] **Step 3: Run local CI-equivalent verification**

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDemoRelease --no-daemon
```

Expected: success with no credentials.

- [ ] **Step 4: Commit**

```powershell
git add .github/workflows/ci.yml apps/android/README.md course-materials/guides/source-maps-demo.md course-materials/guides/setup-android.md
git commit -m "ci(android): verify minified Sentry demo build"
```

### Task 6: Verify USB installation and server deobfuscation

**Files:**
- No source changes unless verification exposes a defect.

- [ ] **Step 1: Verify the physical device**

```powershell
adb devices -l
```

Expected: the Galaxy S24 appears in `device` state.

- [ ] **Step 2: Build with authenticated artifact upload**

```powershell
$env:SENTRY_AUTH_TOKEN = "sntrys_REPLACE_WITH_ORG_TOKEN"
.\gradlew.bat :app:clean :app:assembleDemoRelease --no-daemon --info
Remove-Item Env:SENTRY_AUTH_TOKEN
```

Expected: mapping and source-bundle upload tasks succeed.

- [ ] **Step 3: Install the exact built APK**

```powershell
adb install -r .\app\build\outputs\apk\demoRelease\app-demoRelease.apk
```

Expected: `Success`.

- [ ] **Step 4: Trigger the deliberate fatal crash and restart**

Use the Error Lab button, restart the app, and wait for the queued crash event.

- [ ] **Step 5: Verify the event in Sentry**

Check the event's release, environment, `debug_meta`, original `com.example.SentryDemoViewModel.triggerUncaughtCrash` frame, and source context around the crash line.

- [ ] **Step 6: Run final repository verification**

```powershell
git status --short
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDemoRelease --no-daemon
```

Expected: clean worktree after committed changes and successful build/test tasks.
