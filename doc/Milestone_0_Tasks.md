# Milestone 0: Foundations

**Scope:** Project setup, module scaffolding, reference data seeds (ChassisRef, MotorRef, GearRatioRef), Room migrations

**ERD Impact:** Reference tables established

---

## Task 0.1: Android Project Initialization

### Description
Create a new Android project with the correct configuration and build settings.

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17+
- Android SDK with API Level 34

### Acceptance Criteria
- [ ] Project compiles and runs on emulator/device
- [ ] App displays a placeholder "Mini 4WD Lab" screen
- [ ] Minimum SDK set to 26 (Android 8.0)
- [ ] Target SDK set to 34 (Android 14)

### Implementation Steps

1. **Create New Project**
   - Open Android Studio → New Project → Empty Compose Activity
   - Name: `Mini4WDLab`
   - Package name: `com.mini4wd.lab`
   - Language: Kotlin
   - Minimum SDK: API 26
   - Build configuration: Kotlin DSL (build.gradle.kts)

2. **Configure root build.gradle.kts**
   ```kotlin
   plugins {
       alias(libs.plugins.android.application) apply false
       alias(libs.plugins.android.library) apply false
       alias(libs.plugins.kotlin.android) apply false
       alias(libs.plugins.kotlin.compose) apply false
       alias(libs.plugins.hilt) apply false
       alias(libs.plugins.ksp) apply false
   }
   ```

3. **Configure gradle/libs.versions.toml**
   ```toml
   [versions]
   agp = "8.2.0"
   kotlin = "1.9.21"
   coreKtx = "1.12.0"
   lifecycleRuntimeKtx = "2.6.2"
   activityCompose = "1.8.2"
   composeBom = "2024.02.00"
   hilt = "2.48.1"
   room = "2.6.1"
   ksp = "1.9.21-1.0.15"
   coroutines = "1.7.3"
   coil = "2.5.0"
   navigation = "2.7.6"

   [libraries]
   androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
   androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
   androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
   androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
   androidx-ui = { group = "androidx.compose.ui", name = "ui" }
   androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
   androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
   androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
   androidx-material3 = { group = "androidx.compose.material3", name = "material3" }

   # Hilt
   hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
   hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
   hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.1.0" }

   # Room
   room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
   room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
   room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

   # Coroutines
   kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }

   # Coil
   coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }

   # Navigation
   androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }

   # Testing
   junit = { group = "junit", name = "junit", version = "4.13.2" }
   androidx-junit = { group = "androidx.test.ext", name = "junit", version = "1.1.5" }
   androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version = "3.5.1" }

   [plugins]
   android-application = { id = "com.android.application", version.ref = "agp" }
   android-library = { id = "com.android.library", version.ref = "agp" }
   kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
   kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
   hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
   ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
   ```

4. **Configure app/build.gradle.kts**
   ```kotlin
   plugins {
       alias(libs.plugins.android.application)
       alias(libs.plugins.kotlin.android)
       alias(libs.plugins.kotlin.compose)
       alias(libs.plugins.hilt)
       alias(libs.plugins.ksp)
   }

   android {
       namespace = "com.mini4wd.lab"
       compileSdk = 34

       defaultConfig {
           applicationId = "com.mini4wd.lab"
           minSdk = 26
           targetSdk = 34
           versionCode = 1
           versionName = "1.0.0"

           testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }

       buildTypes {
           release {
               isMinifyEnabled = true
               proguardFiles(
                   getDefaultProguardFile("proguard-android-optimize.txt"),
                   "proguard-rules.pro"
               )
           }
       }
       compileOptions {
           sourceCompatibility = JavaVersion.VERSION_17
           targetCompatibility = JavaVersion.VERSION_17
       }
       kotlinOptions {
           jvmTarget = "17"
       }
       buildFeatures {
           compose = true
       }
   }

   dependencies {
       implementation(libs.androidx.core.ktx)
       implementation(libs.androidx.lifecycle.runtime.ktx)
       implementation(libs.androidx.activity.compose)
       implementation(platform(libs.androidx.compose.bom))
       implementation(libs.androidx.ui)
       implementation(libs.androidx.ui.graphics)
       implementation(libs.androidx.ui.tooling.preview)
       implementation(libs.androidx.material3)

       // Hilt
       implementation(libs.hilt.android)
       implementation(libs.hilt.navigation.compose)
       ksp(libs.hilt.compiler)

       // Coroutines
       implementation(libs.kotlinx.coroutines.android)

       // Testing
       testImplementation(libs.junit)
       androidTestImplementation(libs.androidx.junit)
       androidTestImplementation(libs.androidx.espresso.core)
       debugImplementation(libs.androidx.ui.tooling)
   }
   ```

5. **Create Application Class**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/Mini4WDLabApplication.kt
   package com.mini4wd.lab

   import android.app.Application
   import dagger.hilt.android.HiltAndroidApp

   @HiltAndroidApp
   class Mini4WDLabApplication : Application()
   ```

6. **Update AndroidManifest.xml**
   ```xml
   <application
       android:name=".Mini4WDLabApplication"
       android:allowBackup="true"
       android:icon="@mipmap/ic_launcher"
       android:label="@string/app_name"
       android:roundIcon="@mipmap/ic_launcher_round"
       android:supportsRtl="true"
       android:theme="@style/Theme.Mini4WDLab">
       <!-- Activities -->
   </application>
   ```

7. **Verify Build**
   - Run `./gradlew assembleDebug`
   - Deploy to emulator and verify app launches

---

## Task 0.2: Multi-Module Project Structure

### Description
Set up the multi-module architecture as specified in the ERG.

### Prerequisites
- Task 0.1 completed

### Acceptance Criteria
- [ ] All modules created and compile successfully
- [ ] Module dependencies are correctly configured
- [ ] Each module has appropriate build.gradle.kts

### Module Structure
```
mini4wd-lab/
├── app/                          # Main application module
├── core/
│   ├── database/                 # Room database, DAOs, entities
│   └── common/                   # Shared utilities, extensions
├── feature/
│   ├── profile/                  # Car profile management
│   ├── timing/                   # Lap timing (placeholder)
│   ├── rpm/                      # RPM measurement (placeholder)
│   └── analytics/                # Analytics (placeholder)
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
    └── libs.versions.toml
```

### Implementation Steps

1. **Update settings.gradle.kts**
   ```kotlin
   pluginManagement {
       repositories {
           google()
           mavenCentral()
           gradlePluginPortal()
       }
   }
   dependencyResolutionManagement {
       repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
       repositories {
           google()
           mavenCentral()
       }
   }

   rootProject.name = "Mini4WDLab"
   include(":app")
   include(":core:database")
   include(":core:common")
   include(":feature:profile")
   include(":feature:timing")
   include(":feature:rpm")
   include(":feature:analytics")
   ```

2. **Create core:common module**

   Create `core/common/build.gradle.kts`:
   ```kotlin
   plugins {
       alias(libs.plugins.android.library)
       alias(libs.plugins.kotlin.android)
   }

   android {
       namespace = "com.mini4wd.lab.core.common"
       compileSdk = 34

       defaultConfig {
           minSdk = 26
           testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }

       compileOptions {
           sourceCompatibility = JavaVersion.VERSION_17
           targetCompatibility = JavaVersion.VERSION_17
       }
       kotlinOptions {
           jvmTarget = "17"
       }
   }

   dependencies {
       implementation(libs.androidx.core.ktx)
       implementation(libs.kotlinx.coroutines.android)

       testImplementation(libs.junit)
   }
   ```

   Create placeholder file:
   ```kotlin
   // core/common/src/main/java/com/mini4wd/lab/core/common/Placeholder.kt
   package com.mini4wd.lab.core.common

   // Placeholder for core common module
   ```

3. **Create core:database module**

   Create `core/database/build.gradle.kts`:
   ```kotlin
   plugins {
       alias(libs.plugins.android.library)
       alias(libs.plugins.kotlin.android)
       alias(libs.plugins.ksp)
       alias(libs.plugins.hilt)
   }

   android {
       namespace = "com.mini4wd.lab.core.database"
       compileSdk = 34

       defaultConfig {
           minSdk = 26
           testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }

       compileOptions {
           sourceCompatibility = JavaVersion.VERSION_17
           targetCompatibility = JavaVersion.VERSION_17
       }
       kotlinOptions {
           jvmTarget = "17"
       }
   }

   dependencies {
       implementation(project(":core:common"))

       implementation(libs.androidx.core.ktx)
       implementation(libs.kotlinx.coroutines.android)

       // Room
       implementation(libs.room.runtime)
       implementation(libs.room.ktx)
       ksp(libs.room.compiler)

       // Hilt
       implementation(libs.hilt.android)
       ksp(libs.hilt.compiler)

       testImplementation(libs.junit)
   }
   ```

4. **Create feature modules** (profile, timing, rpm, analytics)

   Each feature module follows this template. Example for `feature/profile/build.gradle.kts`:
   ```kotlin
   plugins {
       alias(libs.plugins.android.library)
       alias(libs.plugins.kotlin.android)
       alias(libs.plugins.kotlin.compose)
       alias(libs.plugins.ksp)
       alias(libs.plugins.hilt)
   }

   android {
       namespace = "com.mini4wd.lab.feature.profile"
       compileSdk = 34

       defaultConfig {
           minSdk = 26
           testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }

       compileOptions {
           sourceCompatibility = JavaVersion.VERSION_17
           targetCompatibility = JavaVersion.VERSION_17
       }
       kotlinOptions {
           jvmTarget = "17"
       }
       buildFeatures {
           compose = true
       }
   }

   dependencies {
       implementation(project(":core:common"))
       implementation(project(":core:database"))

       implementation(libs.androidx.core.ktx)
       implementation(libs.androidx.lifecycle.runtime.ktx)
       implementation(platform(libs.androidx.compose.bom))
       implementation(libs.androidx.ui)
       implementation(libs.androidx.ui.graphics)
       implementation(libs.androidx.ui.tooling.preview)
       implementation(libs.androidx.material3)

       // Hilt
       implementation(libs.hilt.android)
       implementation(libs.hilt.navigation.compose)
       ksp(libs.hilt.compiler)

       // Coil for images
       implementation(libs.coil.compose)

       testImplementation(libs.junit)
       debugImplementation(libs.androidx.ui.tooling)
   }
   ```

5. **Update app module dependencies**

   Update `app/build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation(project(":core:common"))
       implementation(project(":core:database"))
       implementation(project(":feature:profile"))
       implementation(project(":feature:timing"))
       implementation(project(":feature:rpm"))
       implementation(project(":feature:analytics"))
       // ... existing dependencies
   }
   ```

6. **Verify Multi-Module Build**
   - Run `./gradlew clean build`
   - Ensure all modules compile without errors

---

## Task 0.3: Room Database Setup

### Description
Create the Room database configuration with entities for reference data tables.

### Prerequisites
- Task 0.2 completed

### Acceptance Criteria
- [ ] Database compiles without errors
- [ ] All three reference entities created (ChassisRef, MotorRef, GearRatioRef)
- [ ] DAOs created for each entity
- [ ] Database singleton configured with Hilt
- [ ] Room schema export path configured so `exportSchema = true` builds succeed

### Implementation Steps

0. **Configure Room schema export (core/database/build.gradle.kts)**
   ```kotlin
   android {
       // ...
   }

   dependencies {
       // ...
   }

   ksp {
       arg("room.schemaLocation", "$projectDir/schemas")
   }
   ```
   - Create the directory `core/database/schemas` (committed) so KSP can write schemas.

1. **Create ChassisRef Entity**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/entity/ChassisRefEntity.kt
   package com.mini4wd.lab.core.database.entity

   import androidx.room.ColumnInfo
   import androidx.room.Entity
   import androidx.room.PrimaryKey

   @Entity(tableName = "chassis_ref")
   data class ChassisRefEntity(
       @PrimaryKey(autoGenerate = true)
       val id: Long = 0,

       @ColumnInfo(name = "code")
       val code: String,  // e.g., "MS", "MA", "AR"

       @ColumnInfo(name = "name")
       val name: String,  // e.g., "MS Chassis", "MA Chassis"

       @ColumnInfo(name = "shaft_type")
       val shaftType: String,  // "single" or "double"

       @ColumnInfo(name = "motor_position")
       val motorPosition: String  // "front", "center", "rear"
   )
   ```

2. **Create MotorRef Entity**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/entity/MotorRefEntity.kt
   package com.mini4wd.lab.core.database.entity

   import androidx.room.ColumnInfo
   import androidx.room.Entity
   import androidx.room.PrimaryKey

   @Entity(tableName = "motor_ref")
   data class MotorRefEntity(
       @PrimaryKey(autoGenerate = true)
       val id: Long = 0,

       @ColumnInfo(name = "name")
       val name: String,  // e.g., "Torque-Tuned 2", "Hyper-Dash 3"

       @ColumnInfo(name = "shaft_type")
       val shaftType: String,  // "single" or "double"

       @ColumnInfo(name = "category")
       val category: String,  // "stock", "tuned", "dash"

       @ColumnInfo(name = "rpm_min")
       val rpmMin: Int,

       @ColumnInfo(name = "rpm_max")
       val rpmMax: Int
   )
   ```

3. **Create GearRatioRef Entity**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/entity/GearRatioRefEntity.kt
   package com.mini4wd.lab.core.database.entity

   import androidx.room.ColumnInfo
   import androidx.room.Entity
   import androidx.room.PrimaryKey

   @Entity(tableName = "gear_ratio_ref")
   data class GearRatioRefEntity(
       @PrimaryKey(autoGenerate = true)
       val id: Long = 0,

       @ColumnInfo(name = "ratio")
       val ratio: String,  // e.g., "3.5:1", "4:1"

       @ColumnInfo(name = "ratio_value")
       val ratioValue: Float,  // e.g., 3.5, 4.0 for calculations

       @ColumnInfo(name = "shaft_type")
       val shaftType: String,  // "single" or "double"

       @ColumnInfo(name = "gear1_teeth")
       val gear1Teeth: Int,  // Spur gear teeth

       @ColumnInfo(name = "gear1_color")
       val gear1Color: String,  // Spur gear color

       @ColumnInfo(name = "gear1_code")
       val gear1Code: String,  // e.g., "G18", "G22"

       @ColumnInfo(name = "gear2_teeth")
       val gear2Teeth: Int,  // Pinion/Counter gear teeth

       @ColumnInfo(name = "gear2_color")
       val gear2Color: String,  // Pinion/Counter gear color

       @ColumnInfo(name = "gear2_code")
       val gear2Code: String  // e.g., "G17", "G21"
   )
   ```

4. **Create DAOs**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/dao/ChassisRefDao.kt
   package com.mini4wd.lab.core.database.dao

   import androidx.room.Dao
   import androidx.room.Insert
   import androidx.room.OnConflictStrategy
   import androidx.room.Query
   import com.mini4wd.lab.core.database.entity.ChassisRefEntity
   import kotlinx.coroutines.flow.Flow

   @Dao
   interface ChassisRefDao {
       @Query("SELECT * FROM chassis_ref ORDER BY code ASC")
       fun getAllChassis(): Flow<List<ChassisRefEntity>>

       @Query("SELECT * FROM chassis_ref WHERE shaft_type = :shaftType ORDER BY code ASC")
       fun getChassisByShaftType(shaftType: String): Flow<List<ChassisRefEntity>>

       @Query("SELECT * FROM chassis_ref WHERE id = :id")
       suspend fun getChassisById(id: Long): ChassisRefEntity?

       @Query("SELECT * FROM chassis_ref WHERE code = :code")
       suspend fun getChassisByCode(code: String): ChassisRefEntity?

       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insertAll(chassis: List<ChassisRefEntity>)

       @Query("SELECT COUNT(*) FROM chassis_ref")
       suspend fun getCount(): Int
   }
   ```

   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/dao/MotorRefDao.kt
   package com.mini4wd.lab.core.database.dao

   import androidx.room.Dao
   import androidx.room.Insert
   import androidx.room.OnConflictStrategy
   import androidx.room.Query
   import com.mini4wd.lab.core.database.entity.MotorRefEntity
   import kotlinx.coroutines.flow.Flow

   @Dao
   interface MotorRefDao {
       @Query("SELECT * FROM motor_ref ORDER BY rpm_min ASC")
       fun getAllMotors(): Flow<List<MotorRefEntity>>

       @Query("SELECT * FROM motor_ref WHERE shaft_type = :shaftType ORDER BY rpm_min ASC")
       fun getMotorsByShaftType(shaftType: String): Flow<List<MotorRefEntity>>

       @Query("SELECT * FROM motor_ref WHERE category = :category ORDER BY rpm_min ASC")
       fun getMotorsByCategory(category: String): Flow<List<MotorRefEntity>>

       @Query("SELECT * FROM motor_ref WHERE id = :id")
       suspend fun getMotorById(id: Long): MotorRefEntity?

       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insertAll(motors: List<MotorRefEntity>)

       @Query("SELECT COUNT(*) FROM motor_ref")
       suspend fun getCount(): Int
   }
   ```

   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/dao/GearRatioRefDao.kt
   package com.mini4wd.lab.core.database.dao

   import androidx.room.Dao
   import androidx.room.Insert
   import androidx.room.OnConflictStrategy
   import androidx.room.Query
   import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
   import kotlinx.coroutines.flow.Flow

   @Dao
   interface GearRatioRefDao {
       @Query("SELECT * FROM gear_ratio_ref ORDER BY ratio_value ASC")
       fun getAllGearRatios(): Flow<List<GearRatioRefEntity>>

       @Query("SELECT * FROM gear_ratio_ref WHERE shaft_type = :shaftType ORDER BY ratio_value ASC")
       fun getGearRatiosByShaftType(shaftType: String): Flow<List<GearRatioRefEntity>>

       @Query("SELECT * FROM gear_ratio_ref WHERE id = :id")
       suspend fun getGearRatioById(id: Long): GearRatioRefEntity?

       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insertAll(gearRatios: List<GearRatioRefEntity>)

       @Query("SELECT COUNT(*) FROM gear_ratio_ref")
       suspend fun getCount(): Int
   }
   ```

5. **Create Database Class**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/Mini4WDDatabase.kt
   package com.mini4wd.lab.core.database

   import androidx.room.Database
   import androidx.room.RoomDatabase
   import com.mini4wd.lab.core.database.dao.ChassisRefDao
   import com.mini4wd.lab.core.database.dao.GearRatioRefDao
   import com.mini4wd.lab.core.database.dao.MotorRefDao
   import com.mini4wd.lab.core.database.entity.ChassisRefEntity
   import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
   import com.mini4wd.lab.core.database.entity.MotorRefEntity

   @Database(
       entities = [
           ChassisRefEntity::class,
           MotorRefEntity::class,
           GearRatioRefEntity::class
       ],
       version = 1,
       exportSchema = true
   )
   abstract class Mini4WDDatabase : RoomDatabase() {
       abstract fun chassisRefDao(): ChassisRefDao
       abstract fun motorRefDao(): MotorRefDao
       abstract fun gearRatioRefDao(): GearRatioRefDao
   }
   ```

6. **Create Database Module for Hilt**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/di/DatabaseModule.kt
   package com.mini4wd.lab.core.database.di

   import android.content.Context
   import androidx.room.Room
   import com.mini4wd.lab.core.database.Mini4WDDatabase
   import com.mini4wd.lab.core.database.dao.ChassisRefDao
   import com.mini4wd.lab.core.database.dao.GearRatioRefDao
   import com.mini4wd.lab.core.database.dao.MotorRefDao
   import dagger.Module
   import dagger.Provides
   import dagger.hilt.InstallIn
   import dagger.hilt.android.qualifiers.ApplicationContext
   import dagger.hilt.components.SingletonComponent
   import javax.inject.Singleton

   @Module
   @InstallIn(SingletonComponent::class)
   object DatabaseModule {

       @Provides
       @Singleton
       fun provideDatabase(@ApplicationContext context: Context): Mini4WDDatabase {
           return Room.databaseBuilder(
               context,
               Mini4WDDatabase::class.java,
               "mini4wd_lab.db"
           ).build()
       }

       @Provides
       fun provideChassisRefDao(database: Mini4WDDatabase): ChassisRefDao {
           return database.chassisRefDao()
       }

       @Provides
       fun provideMotorRefDao(database: Mini4WDDatabase): MotorRefDao {
           return database.motorRefDao()
       }

       @Provides
       fun provideGearRatioRefDao(database: Mini4WDDatabase): GearRatioRefDao {
           return database.gearRatioRefDao()
       }
   }
   ```

7. **Verify Database Setup**
   - Run `./gradlew :core:database:build`
   - Ensure schema is exported to `core/database/schemas/`

---

## Task 0.4: Reference Data Seeding

### Description
Implement the initial data seeding for chassis, motor, and gear ratio reference tables.

### Prerequisites
- Task 0.3 completed

### Acceptance Criteria
- [ ] All 10 chassis types seeded
- [ ] All 15 motor types seeded (9 single-shaft + 6 double-shaft)
- [ ] All 8 gear ratios seeded (5 single-shaft + 3 double-shaft)
- [ ] Seeding runs only once on first app launch
- [ ] Seeding is transactional to avoid partial data on crash
- [ ] Data is verified via unit tests

### Implementation Steps

1. **Create Reference Data Provider**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/seed/ReferenceDataProvider.kt
   package com.mini4wd.lab.core.database.seed

   import com.mini4wd.lab.core.database.entity.ChassisRefEntity
   import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
   import com.mini4wd.lab.core.database.entity.MotorRefEntity

   object ReferenceDataProvider {

       fun getChassisData(): List<ChassisRefEntity> = listOf(
           // Double Shaft (MS, MA)
           ChassisRefEntity(code = "MS", name = "MS Chassis", shaftType = "double", motorPosition = "center"),
           ChassisRefEntity(code = "MA", name = "MA Chassis", shaftType = "double", motorPosition = "center"),

           // Single Shaft - Rear Motor
           ChassisRefEntity(code = "AR", name = "AR Chassis", shaftType = "single", motorPosition = "rear"),
           ChassisRefEntity(code = "VZ", name = "VZ Chassis", shaftType = "single", motorPosition = "rear"),
           ChassisRefEntity(code = "VS", name = "VS Chassis", shaftType = "single", motorPosition = "rear"),
           ChassisRefEntity(code = "Super-II", name = "Super-II Chassis", shaftType = "single", motorPosition = "rear"),
           ChassisRefEntity(code = "Super-TZ", name = "Super-TZ Chassis", shaftType = "single", motorPosition = "rear"),
           ChassisRefEntity(code = "Super-XX", name = "Super-XX Chassis", shaftType = "single", motorPosition = "rear"),

           // Single Shaft - Front Motor
           ChassisRefEntity(code = "FM-A", name = "FM-A Chassis", shaftType = "single", motorPosition = "front"),
           ChassisRefEntity(code = "Super FM", name = "Super FM Chassis", shaftType = "single", motorPosition = "front")
       )

       fun getMotorData(): List<MotorRefEntity> = listOf(
           // Single Shaft Motors
           MotorRefEntity(name = "Normal Motor", shaftType = "single", category = "stock", rpmMin = 12000, rpmMax = 13000),
           MotorRefEntity(name = "Torque-Tuned 2", shaftType = "single", category = "tuned", rpmMin = 12800, rpmMax = 14700),
           MotorRefEntity(name = "Atomic-Tuned 2", shaftType = "single", category = "tuned", rpmMin = 12700, rpmMax = 14900),
           MotorRefEntity(name = "Rev-Tuned 2", shaftType = "single", category = "tuned", rpmMin = 13400, rpmMax = 15200),
           MotorRefEntity(name = "Light-Dash", shaftType = "single", category = "dash", rpmMin = 14600, rpmMax = 17800),
           MotorRefEntity(name = "Hyper-Dash 3", shaftType = "single", category = "dash", rpmMin = 17200, rpmMax = 21200),
           MotorRefEntity(name = "Power-Dash", shaftType = "single", category = "dash", rpmMin = 19900, rpmMax = 23600),
           MotorRefEntity(name = "Sprint-Dash", shaftType = "single", category = "dash", rpmMin = 20700, rpmMax = 27200),
           MotorRefEntity(name = "Mach-Dash", shaftType = "single", category = "dash", rpmMin = 25000, rpmMax = 28000),

           // Double Shaft Motors (PRO)
           MotorRefEntity(name = "Torque-Tuned 2 PRO", shaftType = "double", category = "tuned", rpmMin = 12200, rpmMax = 14400),
           MotorRefEntity(name = "Atomic-Tuned 2 PRO", shaftType = "double", category = "tuned", rpmMin = 12300, rpmMax = 14500),
           MotorRefEntity(name = "Rev-Tuned 2 PRO", shaftType = "double", category = "tuned", rpmMin = 13200, rpmMax = 14900),
           MotorRefEntity(name = "Light-Dash PRO", shaftType = "double", category = "dash", rpmMin = 14000, rpmMax = 17800),
           MotorRefEntity(name = "Hyper-Dash PRO", shaftType = "double", category = "dash", rpmMin = 17200, rpmMax = 21200),
           MotorRefEntity(name = "Mach-Dash PRO", shaftType = "double", category = "dash", rpmMin = 20000, rpmMax = 24500)
       )

       fun getGearRatioData(): List<GearRatioRefEntity> = listOf(
           // Single Shaft Gear Ratios (Spur + Pinion)
           GearRatioRefEntity(
               ratio = "3.5:1", ratioValue = 3.5f, shaftType = "single",
               gear1Teeth = 35, gear1Color = "Yellow", gear1Code = "G18",
               gear2Teeth = 10, gear2Color = "Light Blue", gear2Code = "G17"
           ),
           GearRatioRefEntity(
               ratio = "3.7:1", ratioValue = 3.7f, shaftType = "single",
               gear1Teeth = 37, gear1Color = "Yellow", gear1Code = "G18",
               gear2Teeth = 10, gear2Color = "Green", gear2Code = "G24"
           ),
           GearRatioRefEntity(
               ratio = "4:1", ratioValue = 4.0f, shaftType = "single",
               gear1Teeth = 36, gear1Color = "Light Brown", gear1Code = "G11",
               gear2Teeth = 9, gear2Color = "Black", gear2Code = "G14"
           ),
           GearRatioRefEntity(
               ratio = "4.2:1", ratioValue = 4.2f, shaftType = "single",
               gear1Teeth = 38, gear1Color = "Light Brown", gear1Code = "G11",
               gear2Teeth = 9, gear2Color = "Red", gear2Code = "G9"
           ),
           GearRatioRefEntity(
               ratio = "5:1", ratioValue = 5.0f, shaftType = "single",
               gear1Teeth = 40, gear1Color = "Light Green", gear1Code = "G6",
               gear2Teeth = 8, gear2Color = "Blue", gear2Code = "G10"
           ),

           // Double Shaft Gear Ratios (Spur + Counter)
           GearRatioRefEntity(
               ratio = "3.5:1", ratioValue = 3.5f, shaftType = "double",
               gear1Teeth = 0, gear1Color = "Pink", gear1Code = "G22",
               gear2Teeth = 0, gear2Color = "Light Green", gear2Code = "G21"
           ),
           GearRatioRefEntity(
               ratio = "3.7:1", ratioValue = 3.7f, shaftType = "double",
               gear1Teeth = 0, gear1Color = "Pink", gear1Code = "G22",
               gear2Teeth = 0, gear2Color = "Yellow", gear2Code = "G23"
           ),
           GearRatioRefEntity(
               ratio = "4:1", ratioValue = 4.0f, shaftType = "double",
               gear1Teeth = 0, gear1Color = "Orange", gear1Code = "G20",
               gear2Teeth = 0, gear2Color = "Blue", gear2Code = "G19"
           )
       )
   }
   ```

2. **Create Database Seeder**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/seed/DatabaseSeeder.kt
   package com.mini4wd.lab.core.database.seed

   import com.mini4wd.lab.core.database.Mini4WDDatabase
   import com.mini4wd.lab.core.database.dao.ChassisRefDao
   import com.mini4wd.lab.core.database.dao.GearRatioRefDao
   import com.mini4wd.lab.core.database.dao.MotorRefDao
   import javax.inject.Inject
   import javax.inject.Singleton

   @Singleton
   class DatabaseSeeder @Inject constructor(
       private val database: Mini4WDDatabase,
       private val chassisRefDao: ChassisRefDao,
       private val motorRefDao: MotorRefDao,
       private val gearRatioRefDao: GearRatioRefDao
   ) {
       suspend fun seedIfEmpty() {
           database.withTransaction {
               if (chassisRefDao.getCount() == 0) {
                   chassisRefDao.insertAll(ReferenceDataProvider.getChassisData())
               }

               if (motorRefDao.getCount() == 0) {
                   motorRefDao.insertAll(ReferenceDataProvider.getMotorData())
               }

               if (gearRatioRefDao.getCount() == 0) {
                   gearRatioRefDao.insertAll(ReferenceDataProvider.getGearRatioData())
               }
           }
       }
   }
   ```

3. **Create Application Initializer**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/startup/DatabaseInitializer.kt
   package com.mini4wd.lab.startup

   import com.mini4wd.lab.core.database.seed.DatabaseSeeder
   import kotlinx.coroutines.CoroutineScope
   import kotlinx.coroutines.Dispatchers
   import kotlinx.coroutines.launch
   import javax.inject.Inject
   import javax.inject.Singleton

   @Singleton
   class DatabaseInitializer @Inject constructor(
       private val databaseSeeder: DatabaseSeeder
   ) {
       fun initialize() {
           CoroutineScope(Dispatchers.IO).launch {
               databaseSeeder.seedIfEmpty()
           }
       }
   }
   ```

4. **Trigger Initialization in Application Class**
   ```kotlin
   // Update Mini4WDLabApplication.kt
   @HiltAndroidApp
   class Mini4WDLabApplication : Application() {

       @Inject
       lateinit var databaseInitializer: DatabaseInitializer

       override fun onCreate() {
           super.onCreate()
           databaseInitializer.initialize()
       }
   }
   ```

5. **Create Unit Tests for Reference Data**
   ```kotlin
   // core/database/src/test/java/com/mini4wd/lab/core/database/ReferenceDataProviderTest.kt
   package com.mini4wd.lab.core.database

   import com.mini4wd.lab.core.database.seed.ReferenceDataProvider
   import org.junit.Assert.assertEquals
   import org.junit.Test

   class ReferenceDataProviderTest {

       @Test
       fun `chassis data has correct count`() {
           val chassisData = ReferenceDataProvider.getChassisData()
           assertEquals(10, chassisData.size)
       }

       @Test
       fun `chassis data has correct shaft types`() {
           val chassisData = ReferenceDataProvider.getChassisData()
           val doubleShaft = chassisData.filter { it.shaftType == "double" }
           val singleShaft = chassisData.filter { it.shaftType == "single" }

           assertEquals(2, doubleShaft.size)  // MS, MA
           assertEquals(8, singleShaft.size)
       }

       @Test
       fun `motor data has correct count`() {
           val motorData = ReferenceDataProvider.getMotorData()
           assertEquals(15, motorData.size)
       }

       @Test
       fun `motor data has correct shaft distribution`() {
           val motorData = ReferenceDataProvider.getMotorData()
           val singleShaft = motorData.filter { it.shaftType == "single" }
           val doubleShaft = motorData.filter { it.shaftType == "double" }

           assertEquals(9, singleShaft.size)
           assertEquals(6, doubleShaft.size)
       }

       @Test
       fun `gear ratio data has correct count`() {
           val gearData = ReferenceDataProvider.getGearRatioData()
           assertEquals(8, gearData.size)
       }

       @Test
       fun `gear ratio data has correct shaft distribution`() {
           val gearData = ReferenceDataProvider.getGearRatioData()
           val singleShaft = gearData.filter { it.shaftType == "single" }
           val doubleShaft = gearData.filter { it.shaftType == "double" }

           assertEquals(5, singleShaft.size)
           assertEquals(3, doubleShaft.size)
       }
   }
   ```

---

## Task 0.5: Basic Navigation Shell

### Description
Create the app shell with bottom navigation structure as defined in the ERG.

### Prerequisites
- Task 0.2 completed

### Acceptance Criteria
- [ ] Bottom navigation with 4 destinations (Garage, Timer, RPM, Stats)
- [ ] Navigation between screens works correctly
- [ ] Placeholder content displays on each screen
- [ ] Material 3 theming applied
- [ ] Dark mode support following system settings

### Implementation Steps

1. **Add Navigation Dependencies to libs.versions.toml**
   ```toml
   [versions]
   navigation = "2.7.6"

   [libraries]
   androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }
   ```

2. **Create Navigation Destinations**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/navigation/Destinations.kt
   package com.mini4wd.lab.navigation

   import androidx.compose.material.icons.Icons
   import androidx.compose.material.icons.filled.DirectionsCar
   import androidx.compose.material.icons.filled.Speed
   import androidx.compose.material.icons.filled.Timer
   import androidx.compose.material.icons.filled.Analytics
   import androidx.compose.ui.graphics.vector.ImageVector

   sealed class Screen(
       val route: String,
       val title: String,
       val icon: ImageVector
   ) {
       data object Garage : Screen("garage", "Garage", Icons.Default.DirectionsCar)
       data object Timer : Screen("timer", "Timer", Icons.Default.Timer)
       data object Rpm : Screen("rpm", "RPM", Icons.Default.Speed)
       data object Stats : Screen("stats", "Stats", Icons.Default.Analytics)
   }

   val bottomNavScreens = listOf(
       Screen.Garage,
       Screen.Timer,
       Screen.Rpm,
       Screen.Stats
   )
   ```

3. **Create Theme**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/ui/theme/Theme.kt
   package com.mini4wd.lab.ui.theme

   import android.app.Activity
   import android.os.Build
   import androidx.compose.foundation.isSystemInDarkTheme
   import androidx.compose.material3.MaterialTheme
   import androidx.compose.material3.darkColorScheme
   import androidx.compose.material3.dynamicDarkColorScheme
   import androidx.compose.material3.dynamicLightColorScheme
   import androidx.compose.material3.lightColorScheme
   import androidx.compose.runtime.Composable
   import androidx.compose.runtime.SideEffect
   import androidx.compose.ui.graphics.toArgb
   import androidx.compose.ui.platform.LocalContext
   import androidx.compose.ui.platform.LocalView
   import androidx.core.view.WindowCompat

   private val DarkColorScheme = darkColorScheme(
       primary = Purple80,
       secondary = PurpleGrey80,
       tertiary = Pink80
   )

   private val LightColorScheme = lightColorScheme(
       primary = Purple40,
       secondary = PurpleGrey40,
       tertiary = Pink40
   )

   @Composable
   fun Mini4WDLabTheme(
       darkTheme: Boolean = isSystemInDarkTheme(),
       dynamicColor: Boolean = true,
       content: @Composable () -> Unit
   ) {
       val colorScheme = when {
           dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
               val context = LocalContext.current
               if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
           }
           darkTheme -> DarkColorScheme
           else -> LightColorScheme
       }

       val view = LocalView.current
       if (!view.isInEditMode) {
           SideEffect {
               val window = (view.context as Activity).window
               window.statusBarColor = colorScheme.primary.toArgb()
               WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
           }
       }

       MaterialTheme(
           colorScheme = colorScheme,
           typography = Typography,
           content = content
       )
   }
   ```

4. **Create Placeholder Screens**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/ui/screens/PlaceholderScreens.kt
   package com.mini4wd.lab.ui.screens

   import androidx.compose.foundation.layout.Box
   import androidx.compose.foundation.layout.fillMaxSize
   import androidx.compose.material3.MaterialTheme
   import androidx.compose.material3.Text
   import androidx.compose.runtime.Composable
   import androidx.compose.ui.Alignment
   import androidx.compose.ui.Modifier

   @Composable
   fun GaragePlaceholder() {
       PlaceholderContent("Garage - Coming in M1")
   }

   @Composable
   fun TimerPlaceholder() {
       PlaceholderContent("Timer - Coming in M3")
   }

   @Composable
   fun RpmPlaceholder() {
       PlaceholderContent("RPM - Coming in M4")
   }

   @Composable
   fun StatsPlaceholder() {
       PlaceholderContent("Stats - Coming in M5")
   }

   @Composable
   private fun PlaceholderContent(text: String) {
       Box(
           modifier = Modifier.fillMaxSize(),
           contentAlignment = Alignment.Center
       ) {
           Text(
               text = text,
               style = MaterialTheme.typography.headlineMedium
           )
       }
   }
   ```

5. **Create Main Navigation Host**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/navigation/AppNavHost.kt
   package com.mini4wd.lab.navigation

   import androidx.compose.runtime.Composable
   import androidx.compose.ui.Modifier
   import androidx.navigation.NavHostController
   import androidx.navigation.compose.NavHost
   import androidx.navigation.compose.composable
   import com.mini4wd.lab.ui.screens.GaragePlaceholder
   import com.mini4wd.lab.ui.screens.RpmPlaceholder
   import com.mini4wd.lab.ui.screens.StatsPlaceholder
   import com.mini4wd.lab.ui.screens.TimerPlaceholder

   @Composable
   fun AppNavHost(
       navController: NavHostController,
       modifier: Modifier = Modifier
   ) {
       NavHost(
           navController = navController,
           startDestination = Screen.Garage.route,
           modifier = modifier
       ) {
           composable(Screen.Garage.route) {
               GaragePlaceholder()
           }
           composable(Screen.Timer.route) {
               TimerPlaceholder()
           }
           composable(Screen.Rpm.route) {
               RpmPlaceholder()
           }
           composable(Screen.Stats.route) {
               StatsPlaceholder()
           }
       }
   }
   ```

6. **Create Main App Scaffold**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/ui/MainApp.kt
   package com.mini4wd.lab.ui

   import androidx.compose.foundation.layout.padding
   import androidx.compose.material3.Icon
   import androidx.compose.material3.NavigationBar
   import androidx.compose.material3.NavigationBarItem
   import androidx.compose.material3.Scaffold
   import androidx.compose.material3.Text
   import androidx.compose.runtime.Composable
   import androidx.compose.runtime.getValue
   import androidx.compose.ui.Modifier
   import androidx.navigation.NavDestination.Companion.hierarchy
   import androidx.navigation.NavGraph.Companion.findStartDestination
   import androidx.navigation.compose.currentBackStackEntryAsState
   import androidx.navigation.compose.rememberNavController
   import com.mini4wd.lab.navigation.AppNavHost
   import com.mini4wd.lab.navigation.bottomNavScreens

   @Composable
   fun MainApp() {
       val navController = rememberNavController()
       val navBackStackEntry by navController.currentBackStackEntryAsState()
       val currentDestination = navBackStackEntry?.destination

       Scaffold(
           bottomBar = {
               NavigationBar {
                   bottomNavScreens.forEach { screen ->
                       NavigationBarItem(
                           icon = { Icon(screen.icon, contentDescription = screen.title) },
                           label = { Text(screen.title) },
                           selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                           onClick = {
                               navController.navigate(screen.route) {
                                   popUpTo(navController.graph.findStartDestination().id) {
                                       saveState = true
                                   }
                                   launchSingleTop = true
                                   restoreState = true
                               }
                           }
                       )
                   }
               }
           }
       ) { innerPadding ->
           AppNavHost(
               navController = navController,
               modifier = Modifier.padding(innerPadding)
           )
       }
   }
   ```

7. **Update MainActivity**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/MainActivity.kt
   package com.mini4wd.lab

   import android.os.Bundle
   import androidx.activity.ComponentActivity
   import androidx.activity.compose.setContent
   import androidx.activity.enableEdgeToEdge
   import com.mini4wd.lab.ui.MainApp
   import com.mini4wd.lab.ui.theme.Mini4WDLabTheme
   import dagger.hilt.android.AndroidEntryPoint

   @AndroidEntryPoint
   class MainActivity : ComponentActivity() {
       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           enableEdgeToEdge()
           setContent {
               Mini4WDLabTheme {
                   MainApp()
               }
           }
       }
   }
   ```

8. **Test Navigation**
   - Run app on device/emulator
   - Verify all 4 tabs are clickable
   - Verify placeholder text displays correctly
   - Test dark mode by changing system settings

---

## Task 0.6: CI/CD Pipeline Setup (Optional)

### Description
Set up GitHub Actions for continuous integration.

### Prerequisites
- Project pushed to GitHub repository

### Acceptance Criteria
- [ ] Build runs on every push/PR
- [ ] Unit tests execute automatically
- [ ] Build artifacts are generated

### Implementation Steps

1. **Create GitHub Actions Workflow**
   ```yaml
   # .github/workflows/android.yml
   name: Android CI

   on:
     push:
       branches: [ main, develop ]
     pull_request:
       branches: [ main, develop ]

   jobs:
     build:
       runs-on: ubuntu-latest

       steps:
       - uses: actions/checkout@v4

       - name: Set up JDK 17
         uses: actions/setup-java@v4
         with:
           java-version: '17'
           distribution: 'temurin'
           cache: gradle

       - name: Grant execute permission for gradlew
         run: chmod +x gradlew

       - name: Build with Gradle
         run: ./gradlew build

       - name: Run Unit Tests
         run: ./gradlew test

       - name: Upload build artifacts
         uses: actions/upload-artifact@v4
         with:
           name: app-debug
           path: app/build/outputs/apk/debug/
   ```

---

## Milestone 0 Completion Checklist

- [ ] Task 0.1: Android Project Initialization
- [ ] Task 0.2: Multi-Module Project Structure
- [ ] Task 0.3: Room Database Setup
- [ ] Task 0.4: Reference Data Seeding
- [ ] Task 0.5: Basic Navigation Shell
- [ ] Task 0.6: CI/CD Pipeline Setup (Optional)

**Definition of Done:**
- All modules compile without errors
- Database is created and seeded on first launch
- Navigation shell works with all 4 tabs
- Unit tests pass for reference data
- App launches successfully on Android 8.0+ devices
