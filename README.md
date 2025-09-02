# Simple Trading App — iOS (Kotlin Multiplatform) [WIP]

This project is a simple trading application targeting **iOS**, written in **Kotlin Multiplatform (KMP)**. The shared business logic (networking, models, use‑cases) lives in Kotlin; the iOS UI is in Swift/SwiftUI and links the generated Kotlin/Native **XCFramework**.

<img src="https://github.com/user-attachments/assets/52f6f406-fa7a-452f-b7e2-227da3d38474" alt="iPhone 16 sim" width="300" />

## 1) Prerequisites

* **macOS**: 14+ recommended.
* **Xcode**: 15.x or newer with Command Line Tools (Preferences → Locations).
* **Java**: **JDK 21+**. 
* **Gradle**: Use the project’s **Gradle Wrapper** (`./gradlew`).
* **Kotlin**: Managed via the Kotlin Gradle plugin (**2.2.0**).
* **No CocoaPods needed**.

---

## 2) Project bootstrap (no CocoaPods)

Run these in the **repo root**:

```bash
# Fetch dependencies & compile everything
./gradlew build

# Build the Apple XCFrameworks from the shared KMP module (Debug shown; use Release for App Store)
./gradlew :shared:assembleXCFramework  # or :shared:assembleDebugXCFramework / :shared:assembleReleaseXCFramework

# Open your iOS project/workspace
open iosApp/iosApp.xcodeproj   # or iosApp.xcworkspace if your app uses one
```

> The XCFrameworks are usually output to: `shared/build/XCFrameworks/{Debug|Release}/`

---

## 3) Link the XCFramework in Xcode (SPM/manual)

1. **Add the framework**: In Xcode, select the iOS app target → **General** → **Frameworks, Libraries, and Embedded Content** → **+** → **Add Other…** → **Add Files…** → pick the `*.xcframework` from `shared/build/XCFrameworks/<Config>/`.
2. **Embed & Sign**: Ensure the added framework shows **Embed & Sign**.
3. **Runpath**: Confirm target **Build Settings** → `LD_RUNPATH_SEARCH_PATHS` includes `@executable_path/Frameworks`.
4. **Schemes/Configs**: If you switch Xcode scheme between **Debug/Release**, build the matching XCFramework configuration (or add both) to avoid *module not found* errors.
5. **Architectures**: KMP should declare `iosArm64()` and `iosSimulatorArm64()`; make sure you run on an Apple‑silicon simulator or device that matches.

> Optional: If your Gradle build is configured for Swift Package generation, you can expose the shared module as a Swift package (task name typically `:shared:generateSwiftPackage`). If not configured, the XCFramework approach above is the simplest.

---

## 4) Everyday commands

### Gradle (KMP)

```bash
# Build everything (all targets)
./gradlew build

# Assemble XCFrameworks (aggregate)
./gradlew :shared:assembleXCFramework
# or per-config
./gradlew :shared:assembleDebugXCFramework
./gradlew :shared:assembleReleaseXCFramework

# Run all Kotlin tests
./gradlew :shared:allTests

# Clean outputs
./gradlew clean
```

### Xcode / iOS app

* **GUI**: Open the project, pick a simulator (e.g., *iPhone 15*), press **Run** ▶︎.
* **CLI** (project example):

```bash
xcodebuild \
  -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 15' \
  build

# Run tests
xcodebuild \
  -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -destination 'platform=iOS Simulator,name=iPhone 15' \
  test
```

---

## 5) Configuration & secrets (if applicable)

* Prefer `.xcconfig` for iOS‑only keys and Kotlin build config or `expect/actual` for shared constants.
* Keep API keys out of VCS. Provide sample files like `.env.example` or `Config.sample.xcconfig`.
* For multiple environments, pass Gradle properties (e.g., `-Penv=staging`) and map to Swift via generated headers/constants.

---

## 6) Troubleshooting (SPM/manual)

* **Framework not found / Swift module not found**

  * Build the matching XCFramework **Debug vs Release**.
  * Ensure **Embed & Sign** and correct **Runpath**.
* **Undefined symbols / ABI mismatch**

  * Clean Xcode (**Shift+Cmd+K**) and rebuild XCFrameworks.
  * Verify your Kotlin targets include `iosArm64` and `iosSimulatorArm64`.
* **Wrong Java used by Gradle**

  * `./gradlew --version` to confirm. Set `org.gradle.java.home=` in `gradle.properties` or export `JAVA_HOME`.
* **Xcode can’t see new Kotlin APIs**

  * Increment the KMP framework version (if using SwiftPM) or **clean build folder** and re‑add the XCFramework.

---

## 7) Project structure (typical)

```
.
├── composeApp/  # where most of the project files reside
│   ├── build
│   ├── src/iosMain
│   └── build.gradle.kts
├── iosApp/                 # iOS app (Swift/SwiftUI)
│   ├── iosApp.xcodeproj    # SPM/manual linking (no Pods)
├── build.gradle.kts # root config
├── gradle/
├── gradlew / gradlew.bat
└── settings.gradle.kts
```

---

## 8) Quick recipes

```bash
# 🆕 Fresh clone → full build + iOS linkage (manual XCFramework)
./gradlew build :shared:assembleDebugXCFramework && open iosApp/iosApp.xcodeproj

# 🔁 Switch to Release frameworks for TestFlight/App Store
./gradlew :shared:assembleReleaseXCFramework

# ✅ Run all KMP tests
./gradlew :shared:allTests

# 🧹 Clean everything
./gradlew clean
```

---
