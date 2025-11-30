# Mini 4WD Lab

Android app (Kotlin + Jetpack Compose) to manage Tamiya Mini 4WD profiles, lap timing via camera, and motor RPM via microphone.

## Features (planned)
- Garage: Multiple car profiles with chassis/motor/gear ratio, setup details, and photos
- History: Versioned configuration changes with notes
- Lap Timing: Camera-based detection with calibration and session logging
- RPM: Audio-based FFT for motor speed with confidence scoring
- Analytics: Cross-session trends and comparisons

## Tech Stack
- Kotlin, Jetpack Compose (Material 3)
- Architecture: MVVM + Clean Architecture modules (`:core`, `:feature`)
- Persistence: Room + Hilt DI, Coroutine/Flow
- CameraX for timing; Oboe/KissFFT for audio
- Image loading: Coil

## Module Layout
```
app/                    // Application shell + navigation
core/common/            // Shared models, utilities
core/database/          // Room entities, DAOs, migrations, seeders
feature/profile/        // Profile CRUD, garage UI
feature/timing/         // Lap timing (placeholder until M3)
feature/rpm/            // RPM measurement (placeholder until M4)
feature/analytics/      // Analytics (placeholder until M5)
doc/                    // Requirements and milestone task docs
```

## Getting Started
1) Prereqs: Android Studio Hedgehog (2023.1.1)+, JDK 17, Android SDK API 34; `JAVA_HOME` set.
2) Clone and open in Android Studio.
3) Ensure Gradle sync succeeds (uses Kotlin DSL + version catalogs).
4) Build/Run: `./gradlew assembleDebug` or run from IDE.

### First Launch Notes
- Room schemas exported via KSP to `core/database/schemas`; ensure the folder exists in VCS.
- Reference data (chassis, motor, gear ratios) seeds on first run via `DatabaseSeeder` (transactional).

## Development Milestones
- M0 Foundations: project scaffold, modules, Room reference tables, navigation shell, CI.
- M1 Profile Core: `CarProfile` entity/repo, garage/list UI, create/edit/detail flows, unique-name validation, migration v1→v2.
- M3 Timing: CameraX detection, sessions, laps.
- M4 RPM: Audio capture + FFT, logging.
- M5 Analytics/Polish: Trends, accessibility/perf, backups.
(See `doc/Milestone_0_Tasks.md` and `doc/Milestone_1_Tasks.md` for step-by-step tasks.)

## Testing
- Unit tests: `./gradlew test`
- Instrumented/migration tests: `./gradlew connectedAndroidTest` (includes Room migration test)

## Code Style
- Kotlin official style; Compose best practices; DI via Hilt modules; avoid destructive migrations.

## License
See `LICENSE`.
