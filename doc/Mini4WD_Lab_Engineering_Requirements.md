# **Mini 4WD Lab**

Engineering Requirements Document

| Version | 1.0 |
| :---- | :---- |
| **Date** | November 2025 |
| **Platform** | Android (API Level 26+) |
| **Status** | Draft |

# **Table of Contents**

# **1\. Executive Summary**

## **1.1 Purpose**

Mini 4WD Lab is an Android application designed to help Tamiya Mini 4WD enthusiasts systematically track, analyze, and optimize their car configurations. The app addresses the complex trade-offs inherent in Mini 4WD tuning by providing comprehensive experiment tracking, performance measurement, and historical analysis capabilities.

## **1.2 Target Users**

* Competitive Mini 4WD racers seeking data-driven performance optimization

* Hobbyists experimenting with different configurations

* Racing teams tracking multiple cars and setups

* Enthusiasts documenting their Mini 4WD collection and modifications

## **1.3 Key Features Overview**

* Car Profile Management: Track multiple cars with complete configuration history

* Visual Lap Timing: Camera-based lap time measurement system

* Motor RPM Analysis: Microphone-based motor speed measurement

* Experiment Logging: Document setup changes and correlate with performance data

# **2\. Functional Requirements**

## **2.1 Car Profile Management**

### **2.1.1 Profile Creation and Storage**

1. The system shall allow users to create unlimited car profiles, each with a unique identifier and user-defined name.

2. Each profile shall store the following chassis information:

* Chassis type selection from official Tamiya chassis list (MS, MA, AR, FM-A, VZ, VS, Super-II, etc.)

* Chassis variant/generation where applicable

* Custom modifications flag with free-text description field

3. Each profile shall store motor configuration:

* Motor type selection from Tamiya motor catalog (Torque-Tuned, Atomic-Tuned, Rev-Tuned, Hyper-Dash, Mach-Dash, Sprint-Dash, Power-Dash, etc.)

* Motor break-in status and date

* Estimated motor age/run count

4. Each profile shall store gear ratio configuration with visual representation:

* Gear ratio value (e.g., 3.5:1, 3.7:1, 4:1, 4.2:1, 5:1)

* Visual color-coded gear indicator matching Tamiya's color system (Yellow/Light Blue \= 3.5:1, Yellow/Green \= 3.7:1, etc.)

* Spur gear and pinion gear tooth count display

5. The system shall support additional configuration fields for comprehensive setup tracking:

* Tire type, material, and diameter (front/rear independent)

* Wheel type and material

* Roller configuration (front/rear/side, diameter, material, bearing type)

* Mass damper setup (position, weight, swing arm length)

* Brake configuration (height, material, position)

* Body shell type and any aerodynamic modifications

* Total car weight

* Battery type and brand preference

### **2.1.2 Profile Image Management**

1. The system shall allow users to capture and store multiple images per profile using the device camera.

2. The system shall support importing images from the device gallery.

3. Each image shall support annotation capabilities including:

* Text labels to identify specific components

* Timestamp of when the image was captured

* Association with a specific configuration version

4. The system shall organize images into categories: Overview, Chassis, Front Detail, Rear Detail, Side View, Custom.

5. Images shall be stored locally with optional cloud backup capability.

### **2.1.3 Configuration History Tracking**

1. The system shall automatically create a timestamped version entry whenever any configuration field is modified.

2. Each history entry shall display a diff view highlighting what changed from the previous version.

3. Users shall be able to add notes explaining the reason for each configuration change.

4. The system shall provide the ability to revert to any previous configuration version.

5. History entries shall be linkable to lap time records and RPM measurements for correlation analysis.

## **2.2 Camera-Based Lap Timing System**

### **2.2.1 Detection Mechanism**

The lap timing system shall use computer vision to detect when a car passes a designated finish line. The system must account for the high speed of Mini 4WD cars (typically 10-30+ km/h on standard tracks) and the varying lighting conditions of racing environments.

1. The system shall support multiple detection methods:

* Color marker detection: User places a distinctive colored marker at finish line

* Motion detection zone: User defines a rectangular zone where car passage triggers timing

* Pattern recognition: System learns to recognize the specific car being timed

2. The camera shall operate at minimum 60 FPS to ensure accurate detection of fast-moving cars.

3. Detection latency shall not exceed 50ms from car crossing to timestamp registration.

4. The system shall provide a calibration mode allowing users to verify detection accuracy before timing runs.

### **2.2.2 Timing Functionality**

1. Lap times shall be recorded with millisecond precision.

2. The system shall support the following timing modes:

* Single lap: Time one complete lap

* Multi-lap session: Continuously time multiple laps with individual and cumulative display

* Best lap tracking: Highlight personal best within session and all-time best

3. Real-time display shall show: Current lap time, last lap time, best lap time, lap count, and session duration.

4. The system shall support manual override buttons for start/stop/lap in case of detection failure.

5. Audio feedback shall indicate lap completion and new personal best.

### **2.2.3 Session Management**

* Each timing session shall be automatically associated with the currently selected car profile.

* Sessions shall record: Date, time, location (optional GPS), track name (user-defined), environmental conditions (temperature, indoor/outdoor).

* Users shall be able to tag sessions with custom labels for organization.

* The system shall provide session comparison views showing lap time trends across multiple sessions.

## **2.3 Microphone-Based Motor RPM Measurement**

### **2.3.1 Audio Analysis Engine**

The RPM measurement system shall analyze motor sound frequencies to determine rotational speed. Mini 4WD motors typically operate in the 15,000-30,000 RPM range, producing characteristic frequencies that can be detected via Fast Fourier Transform (FFT) analysis.

* The system shall use FFT analysis to identify the dominant frequency corresponding to motor rotation.

* Sampling rate shall be minimum 44.1 kHz to accurately capture high-frequency motor sounds.

* The system shall implement noise filtering to isolate motor sound from ambient noise.

* RPM calculation shall account for the number of motor poles (standard Mini 4WD motors are 3-pole).

* The system shall display confidence level for each RPM reading based on signal clarity.

### **2.3.2 Measurement Modes**

* Static measurement: Motor held stationary for break-in analysis or motor comparison

* Rolling measurement: Attempt to capture RPM during track runs (with acknowledgment of reduced accuracy)

* Break-in tracking: Record RPM over time during motor break-in sessions with graphical display

### **2.3.3 RPM Data Logging**

* RPM measurements shall be stored with timestamp, associated car profile, and motor configuration.

* The system shall track RPM trends over time to monitor motor degradation or break-in progress.

* Export functionality shall support CSV format for external analysis.

# **3\. Non-Functional Requirements**

## **3.1 Performance Requirements**

| Metric | Requirement | Measurement Method |
| ----- | ----- | ----- |
| App Launch Time | \< 2 seconds cold start | Stopwatch from tap to interactive |
| Camera Frame Rate | ≥ 60 FPS sustained | Frame counter during timing mode |
| Lap Detection Latency | \< 50ms | High-speed camera verification |
| RPM Update Rate | 10 updates/second | UI refresh counter |
| Database Query Time | \< 100ms for history retrieval | Profiler measurement |
| Image Load Time | \< 500ms for gallery view | Visual inspection with profiler |

## **3.2 Compatibility Requirements**

* Minimum Android API Level: 26 (Android 8.0 Oreo)

* Target Android API Level: 34 (Android 14\)

* Required device features: Camera with autofocus, Microphone, 2GB+ RAM

* Screen size support: Phones (5"-7") and Tablets (7"-12")

* Orientation support: Portrait primary, Landscape for timing mode

## **3.3 Data Storage Requirements**

* Local SQLite database for all profile and measurement data

* Image storage in app-specific directory with automatic cleanup of orphaned files

* Maximum database size consideration: Support for 100+ profiles with 1000+ history entries each

* Backup/restore functionality via Android backup service

* Optional cloud sync via user's Google Drive (future enhancement)

## **3.4 Security Requirements**

* No personally identifiable information collected without consent

* Camera and microphone permissions requested only when features are accessed

* Local data encrypted at rest using Android Keystore

* No network communication required for core functionality

## **3.5 Usability Requirements**

* All primary functions accessible within 3 taps from home screen

* Dark mode support following system settings

* Support for Android accessibility services (TalkBack, font scaling)

* Offline-first design: All features functional without internet connection

* Intuitive visual feedback for all detection and measurement operations

# **4\. Technical Architecture**

## **4.1 Technology Stack**

| Component | Technology |
| ----- | ----- |
| Language | Kotlin (primary), C++ (performance-critical audio/video processing) |
| UI Framework | Jetpack Compose with Material 3 Design |
| Architecture | MVVM with Clean Architecture principles |
| Database | Room Persistence Library (SQLite wrapper) |
| Camera | CameraX API with ML Kit for object detection |
| Audio Processing | Oboe (low-latency audio) \+ KissFFT (frequency analysis) |
| Image Loading | Coil (Kotlin-first image loading library) |
| Dependency Injection | Hilt |
| Async Operations | Kotlin Coroutines \+ Flow |

## **4.2 Module Structure**

The application shall be organized into the following modules:

* :app \- Main application module with UI components

* :core:database \- Room database, DAOs, entities, and migrations

* :core:common \- Shared utilities, extensions, and base classes

* :feature:profile \- Car profile management feature

* :feature:timing \- Lap timing feature with camera integration

* :feature:rpm \- RPM measurement feature with audio processing

* :feature:analytics \- Data analysis and visualization

## **4.3 Database Schema Overview**

### **4.3.1 Logical ERD**

```
ChassisRef      MotorRef        GearRatioRef
     |              |                |
     |              |                |
     |              v                v
              +---------------+
              |   CarProfile  |
              +---------------+
                |1          1|
                |             \
                |              \
                v               v
   +------------------+   +------------------+
   |ConfigurationHist.|   |   ProfileImage   |
   +------------------+   +------------------+
                |1                         1|
                |                           \
                |                            \
                v                             v
        +----------------+          +------------------+
        |  TimingSession |          | RpmMeasurement   |
        +----------------+          +------------------+
                |1
                |
                v
          +-----------+
          | LapRecord |
          +-----------+
```

### **4.3.2 Entities and Fields**

| Entity | Key Fields | Notes |
| ----- | ----- | ----- |
| CarProfile | `id` (PK), `name`, `chassis_ref_id` (FK), `motor_ref_id` (FK), `gear_ratio_ref_id` (FK), `custom_mod_notes`, `tire_front`, `tire_rear`, `wheel_type`, `roller_config`, `damper_config`, `brake_config`, `body_type`, `total_weight_g`, `battery_pref`, `tags`, `created_at`, `updated_at` | Core profile with current configuration snapshot |
| ConfigurationHistory | `id` (PK), `car_profile_id` (FK), `version`, `changed_at`, `changed_by`, `change_reason`, `diff_blob`, `snapshot_blob` | Versioned state and diff for audit/revert |
| ProfileImage | `id` (PK), `car_profile_id` (FK), `config_history_id` (FK nullable), `category` (enum), `file_path`, `captured_at`, `annotation_json`, `source` (camera/gallery) | Image metadata linked to profile and optionally to a configuration version |
| TimingSession | `id` (PK), `car_profile_id` (FK), `started_at`, `ended_at`, `track_name`, `location_lat`, `location_lng`, `environment_notes`, `detection_method`, `calibration_blob` | Session context for lap timing |
| LapRecord | `id` (PK), `timing_session_id` (FK), `lap_number`, `lap_time_ms`, `is_personal_best`, `false_positive_flag`, `captured_at` | Individual laps within a session |
| RpmMeasurement | `id` (PK), `car_profile_id` (FK), `measurement_mode` (enum static/rolling/break-in), `rpm_value`, `confidence_pct`, `sampling_rate_hz`, `noise_floor_db`, `captured_at`, `notes` | Raw RPM data points tied to a profile |
| ChassisRef | `id` (PK), `code`, `name`, `motor_position` | Reference data for chassis |
| MotorRef | `id` (PK), `name`, `category`, `rpm_min`, `rpm_max` | Reference data for motors |
| GearRatioRef | `id` (PK), `ratio`, `spur_teeth`, `pinion_teeth`, `spur_color`, `pinion_color` | Reference data for gear ratios and color coding |

### **4.3.3 Milestones Aligned to ERD Slices**

| Milestone | Scope | ERD Impact |
| ----- | ----- | ----- |
| M0: Foundations | Project setup, module scaffolding, reference data seeds (ChassisRef, MotorRef, GearRatioRef), Room migrations | Reference tables established |
| M1: Profile Core | CRUD for CarProfile, tagging, validation, list/detail UIs | CarProfile |
| M2: History & Media | ConfigurationHistory creation/revert, ProfileImage capture/import/annotation, storage hygiene | ConfigurationHistory, ProfileImage |
| M3: Lap Timing | CameraX timing UI, detection calibration, session storage, lap visualization | TimingSession, LapRecord |
| M4: RPM Measurement | Audio capture pipeline, FFT, confidence scoring, logging, export | RpmMeasurement |
| M5: Analytics & Polishing | Stats module, cross-session comparisons, backups, accessibility & performance tuning | Cross-entity queries/indices |

# **5\. User Interface Specifications**

## **5.1 Navigation Structure**

The app shall use bottom navigation with four primary destinations:

* Garage: Car profile list and management (default landing screen)

* Timer: Camera-based lap timing interface

* RPM: Motor RPM measurement interface

* Stats: Analytics and comparison dashboards

## **5.2 Key Screen Descriptions**

### **5.2.1 Garage Screen**

* Grid or list view of all car profiles with thumbnail images

* Quick-add FAB for creating new profiles

* Search and filter functionality by chassis type, motor, or custom tags

* Swipe actions for quick delete or duplicate

### **5.2.2 Profile Detail Screen**

* Hero image carousel at top with swipe navigation

* Tabbed interface: Specs | History | Images | Stats

* Visual gear ratio indicator with color-coded display matching Tamiya's system

* Edit mode toggle for modifying configuration

* Quick-link buttons to start timing or RPM measurement with this profile

### **5.2.3 Timer Screen**

* Full-screen camera preview with detection zone overlay

* Large, high-contrast time display visible from distance

* Lap list showing recent laps with best lap highlighted

* Floating controls for start/stop/reset with haptic feedback

* Settings drawer for detection method and sensitivity adjustment

### **5.2.4 RPM Screen**

* Real-time frequency spectrum visualizer

* Large RPM numeric display with trend indicator (↑↓↔)

* Recording controls with session duration display

* Historical graph showing RPM over time for current session

* Confidence indicator showing signal quality

# **6\. Data Specifications**

## **6.1 Tamiya Reference Data**

The application shall include pre-populated reference data for Tamiya components:

### **6.1.1 Chassis Types**

| Chassis | Type | Motor Position |
| ----- | ----- | ----- |
| MS | Double Shaft | Center |
| MA | Double Shaft | Center |
| AR | Single Shaft | Rear |
| FM-A | Single Shaft | Front |
| VZ | Single Shaft | Rear |
| VS | Single Shaft | Rear |
| Super-II | Single Shaft | Rear |
| Super-TZ | Single Shaft | Rear |
| Super-XX | Single Shaft | Rear |
| Super FM | Single Shaft | Front |

### **6.1.2 Motor Types**

Mini 4WD motors are categorized by shaft type. Single-shaft motors are used with most chassis types (AR, FM-A, VZ, VS, Super-II, etc.), while double-shaft motors are exclusively for MS and MA chassis which feature center-mounted motor configurations.

#### **Single Shaft Motors** (AR, FM-A, VZ, VS, Super-II, Super-TZ, Super-XX, Super FM)

| Motor | Category | Typical RPM Range | Notes |
| ----- | ----- | ----- | ----- |
| Torque-Tuned 2 | Tuned | 12,300-14,700 | Good acceleration, for courses with turns and slopes |
| Atomic-Tuned 2 | Tuned | 12,700-14,900 | Balanced speed and torque, for mixed courses |
| Rev-Tuned 2 | Tuned | 13,400-15,200 | High rev for top speed, for high-speed courses with straights |
| Light-Dash | Dash | 14,600-17,800 | Intermediate between Atomic-Tuned & Hyper-Dash 3 |
| Hyper-Dash 3 | Dash | 17,200-21,200 | Balanced high performance, for variety of course types |
| Power-Dash | Dash | 19,900-23,600 | High torque, for technical courses with slope sections |
| Sprint-Dash | Dash | 20,700-27,200 | High rev for super top speed, for high-speed courses |
| Ultra-Dash | Dash | 20,400-27,500 | Super high speed for all-out time attack (not for official Tamiya races) |
| Plasma-Dash | Dash | 25,000-28,000 | High performance with great torque (not for official Tamiya races) |

#### **Double Shaft Motors** (MS, MA)

| Motor | Category | Typical RPM Range |
| ----- | ----- | ----- |
| Torque-Tuned 2 PRO | Tuned | 12,200-14,400 |
| Atomic-Tuned 2 PRO | Tuned | 12,300-14,500 |
| Rev-Tuned 2 PRO | Tuned | 13,200-14,900 |
| Light-Dash PRO | Dash | 14,000-17,800 |
| Hyper-Dash PRO | Dash | 17,200-21,200 |
| Mach-Dash PRO | Dash | 20,000-24,500 |

### **6.1.3 Gear Ratio Color Coding**

Single-shaft and double-shaft chassis use different gear systems. Single-shaft chassis use a pinion gear and spur gear combination, while double-shaft (MS/MA) chassis use a counter gear and spur gear system.

#### **Single Shaft Gear Ratios** (AR, FM-A, VZ, VS, Super-II, Super-TZ, Super-XX, Super FM)

| Ratio | Spur/Pinion | Spur Color | Pinion Color |
| ----- | ----- | ----- | ----- |
| 3.5:1 | 35T / 10T | Yellow (G18) | Light Blue (G17) |
| 3.7:1 | 37T / 10T | Yellow (G18) | Green (G24) |
| 4:1 | 36T / 9T | Light Brown (G11) | Black (G14) |
| 4.2:1 | 38T / 9T | Light Brown (G11) | Red (G9) |
| 5:1 | 40T / 8T | Light Green (G6) | Blue (G10) |

#### **Double Shaft Gear Ratios** (MS, MA)

| Ratio | Spur/Counter | Spur Color | Counter Color |
| ----- | ----- | ----- | ----- |
| 3.5:1 | Spur (G22) / Counter (G21) | Pink | Light Green |
| 3.7:1 | Spur (G22) / Counter (G23) | Pink | Yellow |
| 4:1 | Spur (G20) / Counter (G19) | Orange | Blue |

# **7\. Acceptance Criteria**

## **7.1 Profile Management Acceptance**

* User can create a new profile with all required fields in under 2 minutes

* Configuration changes are automatically versioned without user intervention

* Profile images display correctly across all supported screen sizes

* History timeline accurately shows all changes with correct timestamps

* Gear ratio visual indicator matches Tamiya's official color coding

## **7.2 Lap Timing Acceptance**

* System detects car passage with ≥95% accuracy in well-lit conditions

* Lap times are accurate within ±10ms compared to professional timing equipment

* Camera preview maintains ≥60 FPS during timing sessions

* System correctly handles course-out scenarios without false lap registrations

* Manual override controls function reliably when detection fails

## **7.3 RPM Measurement Acceptance**

* RPM readings are accurate within ±500 RPM compared to optical tachometer

* System provides stable readings in typical indoor racing environments

* Confidence indicator correctly reflects signal quality

* Break-in tracking shows meaningful RPM progression over time

# **8\. Future Enhancements (Out of Scope for v1.0)**

The following features are identified for potential future releases:

* Cloud synchronization with Google Drive or Firebase

* Multi-car timing for race scenarios with lane detection

* Community features: Share configurations, compare setups

* AI-powered setup recommendations based on track type and conditions

* Integration with Bluetooth-enabled timing systems

* Speed estimation based on track length and lap time

* AR overlay for real-time component identification

* Export to social media with formatted graphics

# **9\. Appendices**

## **9.1 Glossary**

| Term | Definition |
| ----- | ----- |
| Mini 4WD | A line of battery-powered, four-wheel-drive miniature car kits manufactured by Tamiya, designed for racing on specialized tracks. |
| Chassis | The structural frame of the car that holds all components together. Different chassis types have different motor positions and characteristics. |
| Spur Gear | The larger gear attached to the car's axle that meshes with the pinion gear. |
| Pinion Gear | The smaller gear attached to the motor shaft that drives the spur gear. |
| Mass Damper | A weighted component that helps stabilize the car during jumps and landings by absorbing impact energy. |
| Roller | Rotating wheels mounted on the bumpers that guide the car along the track walls. |
| Break-in | The process of running a new motor to wear in the brushes and commutator for optimal performance. |
| Course-out | When a car leaves the track, typically at a jump section or corner. |
| FFT | Fast Fourier Transform \- an algorithm for converting time-domain signals to frequency-domain for analysis. |

## **9.2 Reference Documents**

* Tamiya Mini 4WD Official Guide Book

* Android CameraX Documentation

* Oboe Audio Library Documentation

* Material Design 3 Guidelines

*— End of Document —*
