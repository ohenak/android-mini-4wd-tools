# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Mini 4WD Lab** is an Android application for Tamiya Mini 4WD enthusiasts to track car configurations, measure lap times via camera, and analyze motor RPM via microphone. The project is currently in the **planning/documentation phase** - no code has been implemented yet.

## Build Commands (Once Implemented)

```bash
# Build debug APK
./gradlew assembleDebug

# Run all unit tests
./gradlew test

# Run single module tests
./gradlew :core:database:test
./gradlew :feature:profile:test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean and rebuild
./gradlew clean build
```

## Architecture

### Module Structure
```
:app                    # Main application, navigation shell, Hilt setup
:core:database          # Room database, DAOs, entities, migrations, seeders
:core:common            # Shared utilities, domain models, extensions
:feature:profile        # Car profile CRUD, garage list/detail screens
:feature:timing         # Camera-based lap timing (CameraX + ML Kit)
:feature:rpm            # Microphone-based RPM measurement (Oboe + FFT)
:feature:analytics      # Stats dashboards, cross-session comparisons
```

### Tech Stack
- **Language**: Kotlin (primary), C++ (audio/video processing)
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Database**: Room with migrations
- **Async**: Kotlin Coroutines + Flow

### Key Domain Concepts

**Shaft Types**: Mini 4WD motors/gears are categorized by shaft type:
- **Single shaft**: Used by AR, FM-A, VZ, VS, Super-II, Super-TZ, Super-XX, Super FM chassis
- **Double shaft (PRO)**: Used exclusively by MS and MA chassis

When a user selects a chassis, the motor and gear ratio dropdowns must filter to show only compatible shaft type options.

### Database Entities
- `ChassisRef`, `MotorRef`, `GearRatioRef` - Pre-seeded reference data
- `CarProfile` - Core profile with FK references to above
- `ConfigurationHistory` - Versioned snapshots for audit/revert
- `ProfileImage` - Image metadata with category enum
- `TimingSession`, `LapRecord` - Lap timing data
- `RpmMeasurement` - Motor RPM readings

## Documentation

- [doc/Mini4WD_Lab_Engineering_Requirements.md](doc/Mini4WD_Lab_Engineering_Requirements.md) - Full ERD with all requirements
- [doc/Milestone_0_Tasks.md](doc/Milestone_0_Tasks.md) - Project setup, database, navigation shell
- [doc/Milestone_1_Tasks.md](doc/Milestone_1_Tasks.md) - CarProfile CRUD and UI implementation

## Development Milestones

| Milestone | Focus |
|-----------|-------|
| M0 | Project scaffolding, Room setup, reference data seeding, navigation shell |
| M1 | CarProfile CRUD, garage list/detail screens, validation |
| M2 | ConfigurationHistory, ProfileImage capture/annotation |
| M3 | CameraX lap timing, detection calibration |
| M4 | Oboe audio capture, FFT-based RPM measurement |
| M5 | Analytics dashboards, backup/restore, polish |

## Reference Data Sources

The `doc/` folder contains official Tamiya reference materials:
- `motorguide_en.jpg` - Motor specifications and RPM ranges
- `mini-4wd-motor-chart-v3.jpg` - PRO motor chart for double-shaft
- `2023_en_gr_table.pdf` - Gear ratio compatibility by chassis type
