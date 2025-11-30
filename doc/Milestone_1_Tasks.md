# Milestone 1: Profile Core

**Scope:** CRUD for CarProfile, tagging, validation, list/detail UIs

**ERD Impact:** CarProfile entity

**Prerequisites:** Milestone 0 completed

---

## Task 1.1: CarProfile Entity and DAO

### Description
Create the CarProfile database entity with all required fields and corresponding DAO.

### Prerequisites
- Milestone 0 completed
- Database module functional

### Acceptance Criteria
- [ ] CarProfile entity created with all fields from ERD
- [ ] DAO supports full CRUD operations
- [ ] Foreign key relationships to reference tables established
- [ ] Unique constraint enforced on `name`
- [ ] Room database version bumped with migration from v1 → v2 (no destructive migration)
- [ ] Unit tests pass for DAO operations

### Implementation Steps

1. **Create CarProfile Entity**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/entity/CarProfileEntity.kt
   package com.mini4wd.lab.core.database.entity

   import androidx.room.ColumnInfo
   import androidx.room.Entity
   import androidx.room.ForeignKey
   import androidx.room.Index
   import androidx.room.PrimaryKey

   @Entity(
       tableName = "car_profile",
       foreignKeys = [
           ForeignKey(
               entity = ChassisRefEntity::class,
               parentColumns = ["id"],
               childColumns = ["chassis_ref_id"],
               onDelete = ForeignKey.SET_NULL
           ),
           ForeignKey(
               entity = MotorRefEntity::class,
               parentColumns = ["id"],
               childColumns = ["motor_ref_id"],
               onDelete = ForeignKey.SET_NULL
           ),
           ForeignKey(
               entity = GearRatioRefEntity::class,
               parentColumns = ["id"],
               childColumns = ["gear_ratio_ref_id"],
               onDelete = ForeignKey.SET_NULL
           )
       ],
       indices = [
           Index(value = ["chassis_ref_id"]),
           Index(value = ["motor_ref_id"]),
           Index(value = ["gear_ratio_ref_id"]),
           Index(value = ["name"], unique = true)
       ]
   )
   data class CarProfileEntity(
       @PrimaryKey(autoGenerate = true)
       val id: Long = 0,

       @ColumnInfo(name = "name")
       val name: String,

       @ColumnInfo(name = "chassis_ref_id")
       val chassisRefId: Long?,

       @ColumnInfo(name = "motor_ref_id")
       val motorRefId: Long?,

       @ColumnInfo(name = "gear_ratio_ref_id")
       val gearRatioRefId: Long?,

       // Motor details
       @ColumnInfo(name = "motor_break_in_status")
       val motorBreakInStatus: Boolean = false,

       @ColumnInfo(name = "motor_break_in_date")
       val motorBreakInDate: Long? = null,  // Timestamp

       @ColumnInfo(name = "motor_run_count")
       val motorRunCount: Int = 0,

       // Custom modifications
       @ColumnInfo(name = "custom_mod_notes")
       val customModNotes: String? = null,

       // Tire configuration
       @ColumnInfo(name = "tire_front_type")
       val tireFrontType: String? = null,

       @ColumnInfo(name = "tire_front_material")
       val tireFrontMaterial: String? = null,

       @ColumnInfo(name = "tire_front_diameter_mm")
       val tireFrontDiameterMm: Float? = null,

       @ColumnInfo(name = "tire_rear_type")
       val tireRearType: String? = null,

       @ColumnInfo(name = "tire_rear_material")
       val tireRearMaterial: String? = null,

       @ColumnInfo(name = "tire_rear_diameter_mm")
       val tireRearDiameterMm: Float? = null,

       // Wheel configuration
       @ColumnInfo(name = "wheel_type")
       val wheelType: String? = null,

       @ColumnInfo(name = "wheel_material")
       val wheelMaterial: String? = null,

       // Roller configuration (stored as JSON string)
       @ColumnInfo(name = "roller_config_json")
       val rollerConfigJson: String? = null,

       // Mass damper configuration (stored as JSON string)
       @ColumnInfo(name = "damper_config_json")
       val damperConfigJson: String? = null,

       // Brake configuration (stored as JSON string)
       @ColumnInfo(name = "brake_config_json")
       val brakeConfigJson: String? = null,

       // Body
       @ColumnInfo(name = "body_type")
       val bodyType: String? = null,

       @ColumnInfo(name = "body_aero_notes")
       val bodyAeroNotes: String? = null,

       // Weight and battery
       @ColumnInfo(name = "total_weight_g")
       val totalWeightG: Float? = null,

       @ColumnInfo(name = "battery_type")
       val batteryType: String? = null,

       @ColumnInfo(name = "battery_brand")
       val batteryBrand: String? = null,

       // Tags for filtering (comma-separated)
       @ColumnInfo(name = "tags")
       val tags: String? = null,

       // Thumbnail image path
       @ColumnInfo(name = "thumbnail_path")
       val thumbnailPath: String? = null,

       // Timestamps
       @ColumnInfo(name = "created_at")
       val createdAt: Long = System.currentTimeMillis(),

       @ColumnInfo(name = "updated_at")
       val updatedAt: Long = System.currentTimeMillis()
   )
   ```

2. **Create CarProfile DAO**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/dao/CarProfileDao.kt
   package com.mini4wd.lab.core.database.dao

   import androidx.room.Dao
   import androidx.room.Delete
   import androidx.room.Insert
   import androidx.room.OnConflictStrategy
   import androidx.room.Query
   import androidx.room.Transaction
   import androidx.room.Update
   import com.mini4wd.lab.core.database.entity.CarProfileEntity
   import kotlinx.coroutines.flow.Flow

   @Dao
   interface CarProfileDao {

       // Create
       @Insert(onConflict = OnConflictStrategy.ABORT)
       suspend fun insert(profile: CarProfileEntity): Long

       // Read - All profiles
       @Query("SELECT * FROM car_profile ORDER BY updated_at DESC")
       fun getAllProfiles(): Flow<List<CarProfileEntity>>

       // Read - Single profile by ID
       @Query("SELECT * FROM car_profile WHERE id = :id")
       fun getProfileById(id: Long): Flow<CarProfileEntity?>

       @Query("SELECT * FROM car_profile WHERE id = :id")
       suspend fun getProfileByIdOnce(id: Long): CarProfileEntity?

       // Read - Search by name
       @Query("SELECT * FROM car_profile WHERE name LIKE '%' || :query || '%' ORDER BY updated_at DESC")
       fun searchByName(query: String): Flow<List<CarProfileEntity>>

       // Read - Filter by chassis type
       @Query("SELECT * FROM car_profile WHERE chassis_ref_id = :chassisRefId ORDER BY updated_at DESC")
       fun getProfilesByChassisType(chassisRefId: Long): Flow<List<CarProfileEntity>>

       // Read - Filter by motor type
       @Query("SELECT * FROM car_profile WHERE motor_ref_id = :motorRefId ORDER BY updated_at DESC")
       fun getProfilesByMotorType(motorRefId: Long): Flow<List<CarProfileEntity>>

       // Read - Filter by tag (partial match)
       @Query("SELECT * FROM car_profile WHERE tags LIKE '%' || :tag || '%' ORDER BY updated_at DESC")
       fun getProfilesByTag(tag: String): Flow<List<CarProfileEntity>>

       // Update
       @Update
       suspend fun update(profile: CarProfileEntity)

       // Update timestamp only
       @Query("UPDATE car_profile SET updated_at = :timestamp WHERE id = :id")
       suspend fun updateTimestamp(id: Long, timestamp: Long = System.currentTimeMillis())

       // Delete
       @Delete
       suspend fun delete(profile: CarProfileEntity)

       @Query("DELETE FROM car_profile WHERE id = :id")
       suspend fun deleteById(id: Long)

       // Count
       @Query("SELECT COUNT(*) FROM car_profile")
       suspend fun getCount(): Int

       // Check if name exists (for validation)
       @Query("SELECT COUNT(*) FROM car_profile WHERE name = :name AND id != :excludeId")
       suspend fun countByName(name: String, excludeId: Long = 0): Int
   }
   ```

3. **Create CarProfile with Relations (for queries that need joined data)**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/entity/CarProfileWithRefs.kt
   package com.mini4wd.lab.core.database.entity

   import androidx.room.Embedded
   import androidx.room.Relation

   data class CarProfileWithRefs(
       @Embedded
       val profile: CarProfileEntity,

       @Relation(
           parentColumn = "chassis_ref_id",
           entityColumn = "id"
       )
       val chassis: ChassisRefEntity?,

       @Relation(
           parentColumn = "motor_ref_id",
           entityColumn = "id"
       )
       val motor: MotorRefEntity?,

       @Relation(
           parentColumn = "gear_ratio_ref_id",
           entityColumn = "id"
       )
       val gearRatio: GearRatioRefEntity?
   )
   ```

4. **Add Relation Query to DAO**
   ```kotlin
   // Add to CarProfileDao.kt
   @Transaction
   @Query("SELECT * FROM car_profile WHERE id = :id")
   fun getProfileWithRefsById(id: Long): Flow<CarProfileWithRefs?>

   @Transaction
   @Query("SELECT * FROM car_profile ORDER BY updated_at DESC")
   fun getAllProfilesWithRefs(): Flow<List<CarProfileWithRefs>>
   ```

5. **Update Database Class**
   ```kotlin
   // Update Mini4WDDatabase.kt
   @Database(
       entities = [
           ChassisRefEntity::class,
           MotorRefEntity::class,
           GearRatioRefEntity::class,
           CarProfileEntity::class  // Add this
       ],
       version = 2,  // Increment version
       exportSchema = true
   )
   abstract class Mini4WDDatabase : RoomDatabase() {
       abstract fun chassisRefDao(): ChassisRefDao
       abstract fun motorRefDao(): MotorRefDao
       abstract fun gearRatioRefDao(): GearRatioRefDao
       abstract fun carProfileDao(): CarProfileDao  // Add this
   }
   ```

6. **Create Migration**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/migration/Migrations.kt
   package com.mini4wd.lab.core.database.migration

   import androidx.room.migration.Migration
   import androidx.sqlite.db.SupportSQLiteDatabase

   val MIGRATION_1_2 = object : Migration(1, 2) {
       override fun migrate(database: SupportSQLiteDatabase) {
           database.execSQL("""
               CREATE TABLE IF NOT EXISTS car_profile (
                   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                   name TEXT NOT NULL,
                   chassis_ref_id INTEGER,
                   motor_ref_id INTEGER,
                   gear_ratio_ref_id INTEGER,
                   motor_break_in_status INTEGER NOT NULL DEFAULT 0,
                   motor_break_in_date INTEGER,
                   motor_run_count INTEGER NOT NULL DEFAULT 0,
                   custom_mod_notes TEXT,
                   tire_front_type TEXT,
                   tire_front_material TEXT,
                   tire_front_diameter_mm REAL,
                   tire_rear_type TEXT,
                   tire_rear_material TEXT,
                   tire_rear_diameter_mm REAL,
                   wheel_type TEXT,
                   wheel_material TEXT,
                   roller_config_json TEXT,
                   damper_config_json TEXT,
                   brake_config_json TEXT,
                   body_type TEXT,
                   body_aero_notes TEXT,
                   total_weight_g REAL,
                   battery_type TEXT,
                   battery_brand TEXT,
                   tags TEXT,
                   thumbnail_path TEXT,
                   created_at INTEGER NOT NULL,
                   updated_at INTEGER NOT NULL,
                   FOREIGN KEY (chassis_ref_id) REFERENCES chassis_ref(id) ON DELETE SET NULL,
                   FOREIGN KEY (motor_ref_id) REFERENCES motor_ref(id) ON DELETE SET NULL,
                   FOREIGN KEY (gear_ratio_ref_id) REFERENCES gear_ratio_ref(id) ON DELETE SET NULL
               )
           """)
           database.execSQL("CREATE INDEX IF NOT EXISTS index_car_profile_chassis_ref_id ON car_profile(chassis_ref_id)")
           database.execSQL("CREATE INDEX IF NOT EXISTS index_car_profile_motor_ref_id ON car_profile(motor_ref_id)")
           database.execSQL("CREATE INDEX IF NOT EXISTS index_car_profile_gear_ratio_ref_id ON car_profile(gear_ratio_ref_id)")
           database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_car_profile_name ON car_profile(name)")
       }
   }
   ```

7. **Update Database Module**
   ```kotlin
   // Update DatabaseModule.kt
   @Provides
   @Singleton
   fun provideDatabase(@ApplicationContext context: Context): Mini4WDDatabase {
       return Room.databaseBuilder(
           context,
           Mini4WDDatabase::class.java,
           "mini4wd_lab.db"
       )
       .addMigrations(MIGRATION_1_2) // no destructive fallback; keep data
       .build()
   }

   @Provides
   fun provideCarProfileDao(database: Mini4WDDatabase): CarProfileDao {
       return database.carProfileDao()
   }
   ```

8. **Create DAO Unit Tests**
   ```kotlin
   // core/database/src/androidTest/java/com/mini4wd/lab/core/database/CarProfileDaoTest.kt
   package com.mini4wd.lab.core.database

   import android.content.Context
   import androidx.room.Room
   import androidx.test.core.app.ApplicationProvider
   import androidx.test.ext.junit.runners.AndroidJUnit4
   import com.mini4wd.lab.core.database.dao.CarProfileDao
   import com.mini4wd.lab.core.database.entity.CarProfileEntity
   import kotlinx.coroutines.flow.first
   import kotlinx.coroutines.runBlocking
   import org.junit.After
   import org.junit.Assert.*
   import org.junit.Before
   import org.junit.Test
   import org.junit.runner.RunWith

   @RunWith(AndroidJUnit4::class)
   class CarProfileDaoTest {

       private lateinit var database: Mini4WDDatabase
       private lateinit var carProfileDao: CarProfileDao

       @Before
       fun setup() {
           val context = ApplicationProvider.getApplicationContext<Context>()
           database = Room.inMemoryDatabaseBuilder(context, Mini4WDDatabase::class.java)
               .allowMainThreadQueries()
               .build()
           carProfileDao = database.carProfileDao()
       }

       @After
       fun teardown() {
           database.close()
       }

       @Test
       fun insertAndRetrieveProfile() = runBlocking {
           val profile = CarProfileEntity(
               name = "Test Car",
               chassisRefId = null,
               motorRefId = null,
               gearRatioRefId = null
           )

           val id = carProfileDao.insert(profile)
           val retrieved = carProfileDao.getProfileByIdOnce(id)

           assertNotNull(retrieved)
           assertEquals("Test Car", retrieved?.name)
       }

       @Test
       fun updateProfile() = runBlocking {
           val profile = CarProfileEntity(
               name = "Original Name",
               chassisRefId = null,
               motorRefId = null,
               gearRatioRefId = null
           )

           val id = carProfileDao.insert(profile)
           val inserted = carProfileDao.getProfileByIdOnce(id)!!

           val updated = inserted.copy(name = "Updated Name", updatedAt = System.currentTimeMillis())
           carProfileDao.update(updated)

           val retrieved = carProfileDao.getProfileByIdOnce(id)
           assertEquals("Updated Name", retrieved?.name)
       }

       @Test
       fun deleteProfile() = runBlocking {
           val profile = CarProfileEntity(
               name = "To Delete",
               chassisRefId = null,
               motorRefId = null,
               gearRatioRefId = null
           )

           val id = carProfileDao.insert(profile)
           carProfileDao.deleteById(id)

           val retrieved = carProfileDao.getProfileByIdOnce(id)
           assertNull(retrieved)
       }

       @Test
       fun searchByName() = runBlocking {
           carProfileDao.insert(CarProfileEntity(name = "Alpha Car", chassisRefId = null, motorRefId = null, gearRatioRefId = null))
           carProfileDao.insert(CarProfileEntity(name = "Beta Car", chassisRefId = null, motorRefId = null, gearRatioRefId = null))
           carProfileDao.insert(CarProfileEntity(name = "Gamma", chassisRefId = null, motorRefId = null, gearRatioRefId = null))

           val results = carProfileDao.searchByName("Car").first()
           assertEquals(2, results.size)
       }

       @Test
       fun filterByTag() = runBlocking {
           carProfileDao.insert(CarProfileEntity(name = "Car 1", chassisRefId = null, motorRefId = null, gearRatioRefId = null, tags = "race,speed"))
           carProfileDao.insert(CarProfileEntity(name = "Car 2", chassisRefId = null, motorRefId = null, gearRatioRefId = null, tags = "race,torque"))
           carProfileDao.insert(CarProfileEntity(name = "Car 3", chassisRefId = null, motorRefId = null, gearRatioRefId = null, tags = "test"))

           val results = carProfileDao.getProfilesByTag("race").first()
           assertEquals(2, results.size)
       }
   }
   ```

---

## Task 1.2: Domain Layer - CarProfile Repository

### Description
Create the repository layer to abstract data access for car profiles.

### Prerequisites
- Task 1.1 completed

### Acceptance Criteria
- [ ] Repository interface defined
- [ ] Repository implementation with proper error handling
- [ ] Hilt module provides repository
- [ ] Unit tests with mock DAO
- [ ] Tags normalized (trimmed, deduped) and `updated_at` maintained on create/update

### Implementation Steps

1. **Create Domain Models**
   ```kotlin
   // core/common/src/main/java/com/mini4wd/lab/core/common/model/CarProfile.kt
   package com.mini4wd.lab.core.common.model

   data class CarProfile(
       val id: Long = 0,
       val name: String,
       val chassis: ChassisInfo? = null,
       val motor: MotorInfo? = null,
       val gearRatio: GearRatioInfo? = null,
       val tires: TireConfig = TireConfig(),
       val wheels: WheelConfig? = null,
       val rollerConfigJson: String? = null,
       val damperConfigJson: String? = null,
       val brakeConfigJson: String? = null,
       val customModNotes: String? = null,
       val bodyType: String? = null,
       val totalWeightG: Float? = null,
       val batteryType: String? = null,
       val batteryBrand: String? = null,
       val tags: List<String> = emptyList(),
       val thumbnailPath: String? = null,
       val createdAt: Long = System.currentTimeMillis(),
       val updatedAt: Long = System.currentTimeMillis()
   )

   data class ChassisInfo(
       val id: Long,
       val code: String,
       val name: String,
       val shaftType: String,
       val motorPosition: String
   )

   data class MotorInfo(
       val id: Long,
       val name: String,
       val shaftType: String,
       val category: String,
       val rpmMin: Int,
       val rpmMax: Int,
       val breakInStatus: Boolean = false,
       val breakInDate: Long? = null,
       val runCount: Int = 0
   )

   data class GearRatioInfo(
       val id: Long,
       val ratio: String,
       val ratioValue: Float,
       val shaftType: String,
       val gear1Color: String,
       val gear2Color: String
   )

   data class TireConfig(
       val frontType: String? = null,
       val frontMaterial: String? = null,
       val frontDiameterMm: Float? = null,
       val rearType: String? = null,
       val rearMaterial: String? = null,
       val rearDiameterMm: Float? = null
   )

   data class WheelConfig(
       val type: String? = null,
       val material: String? = null
   )
   ```

2. **Create Mappers**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/mapper/CarProfileMapper.kt
   package com.mini4wd.lab.core.database.mapper

   import com.mini4wd.lab.core.common.model.*
   import com.mini4wd.lab.core.database.entity.*

   fun CarProfileWithRefs.toDomain(): CarProfile {
       return CarProfile(
           id = profile.id,
           name = profile.name,
           chassis = chassis?.let {
               ChassisInfo(
                   id = it.id,
                   code = it.code,
                   name = it.name,
                   shaftType = it.shaftType,
                   motorPosition = it.motorPosition
               )
           },
           motor = motor?.let {
               MotorInfo(
                   id = it.id,
                   name = it.name,
                   shaftType = it.shaftType,
                   category = it.category,
                   rpmMin = it.rpmMin,
                   rpmMax = it.rpmMax,
                   breakInStatus = profile.motorBreakInStatus,
                   breakInDate = profile.motorBreakInDate,
                   runCount = profile.motorRunCount
               )
           },
           gearRatio = gearRatio?.let {
               GearRatioInfo(
                   id = it.id,
                   ratio = it.ratio,
                   ratioValue = it.ratioValue,
                   shaftType = it.shaftType,
                   gear1Color = it.gear1Color,
                   gear2Color = it.gear2Color
               )
           },
           tires = TireConfig(
               frontType = profile.tireFrontType,
               frontMaterial = profile.tireFrontMaterial,
               frontDiameterMm = profile.tireFrontDiameterMm,
               rearType = profile.tireRearType,
               rearMaterial = profile.tireRearMaterial,
               rearDiameterMm = profile.tireRearDiameterMm
           ),
           wheels = if (profile.wheelType != null || profile.wheelMaterial != null) {
               WheelConfig(
                   type = profile.wheelType,
                   material = profile.wheelMaterial
               )
           } else null,
           rollerConfigJson = profile.rollerConfigJson,
           damperConfigJson = profile.damperConfigJson,
           brakeConfigJson = profile.brakeConfigJson,
           customModNotes = profile.customModNotes,
           bodyType = profile.bodyType,
           totalWeightG = profile.totalWeightG,
           batteryType = profile.batteryType,
           batteryBrand = profile.batteryBrand,
           tags = profile.tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
           thumbnailPath = profile.thumbnailPath,
           createdAt = profile.createdAt,
           updatedAt = profile.updatedAt
       )
   }

   fun CarProfile.toEntity(): CarProfileEntity {
       return CarProfileEntity(
           id = id,
           name = name,
           chassisRefId = chassis?.id,
           motorRefId = motor?.id,
           gearRatioRefId = gearRatio?.id,
           motorBreakInStatus = motor?.breakInStatus ?: false,
           motorBreakInDate = motor?.breakInDate,
           motorRunCount = motor?.runCount ?: 0,
           customModNotes = customModNotes,
           tireFrontType = tires.frontType,
           tireFrontMaterial = tires.frontMaterial,
           tireFrontDiameterMm = tires.frontDiameterMm,
           tireRearType = tires.rearType,
           tireRearMaterial = tires.rearMaterial,
           tireRearDiameterMm = tires.rearDiameterMm,
           wheelType = wheels?.type,
           wheelMaterial = wheels?.material,
           rollerConfigJson = rollerConfigJson,
           damperConfigJson = damperConfigJson,
           brakeConfigJson = brakeConfigJson,
           bodyType = bodyType,
           totalWeightG = totalWeightG,
           batteryType = batteryType,
           batteryBrand = batteryBrand,
           tags = tags.joinToString(","),
           thumbnailPath = thumbnailPath,
           createdAt = createdAt,
           updatedAt = updatedAt
       )
   }
   ```

3. **Create Repository Interface**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/repository/CarProfileRepository.kt
   package com.mini4wd.lab.core.database.repository

   import com.mini4wd.lab.core.common.model.CarProfile
   import kotlinx.coroutines.flow.Flow

   interface CarProfileRepository {
       fun getAllProfiles(): Flow<List<CarProfile>>
       fun getProfileById(id: Long): Flow<CarProfile?>
       fun searchProfiles(query: String): Flow<List<CarProfile>>
       fun getProfilesByTag(tag: String): Flow<List<CarProfile>>

       suspend fun createProfile(profile: CarProfile): Result<Long>
       suspend fun updateProfile(profile: CarProfile): Result<Unit>
       suspend fun deleteProfile(id: Long): Result<Unit>
       suspend fun isNameUnique(name: String, excludeId: Long = 0): Boolean
   }
   ```

4. **Create Repository Implementation**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/repository/CarProfileRepositoryImpl.kt
   package com.mini4wd.lab.core.database.repository

   import com.mini4wd.lab.core.common.model.CarProfile
   import com.mini4wd.lab.core.database.dao.CarProfileDao
   import com.mini4wd.lab.core.database.mapper.toDomain
   import com.mini4wd.lab.core.database.mapper.toEntity
   import kotlinx.coroutines.flow.Flow
   import kotlinx.coroutines.flow.map
   import javax.inject.Inject
   import javax.inject.Singleton

   @Singleton
   class CarProfileRepositoryImpl @Inject constructor(
       private val carProfileDao: CarProfileDao
   ) : CarProfileRepository {

       override fun getAllProfiles(): Flow<List<CarProfile>> {
           return carProfileDao.getAllProfilesWithRefs().map { list ->
               list.map { it.toDomain() }
           }
       }

       override fun getProfileById(id: Long): Flow<CarProfile?> {
           return carProfileDao.getProfileWithRefsById(id).map { it?.toDomain() }
       }

       override fun searchProfiles(query: String): Flow<List<CarProfile>> {
           return carProfileDao.searchByName(query).map { list ->
               list.map { entity ->
                   // For search, we return without full refs for performance
                   CarProfile(
                       id = entity.id,
                       name = entity.name,
                       tags = entity.tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
                       thumbnailPath = entity.thumbnailPath,
                       createdAt = entity.createdAt,
                       updatedAt = entity.updatedAt
                   )
               }
           }
       }

       override fun getProfilesByTag(tag: String): Flow<List<CarProfile>> {
           return carProfileDao.getProfilesByTag(tag).map { list ->
               list.map { entity ->
                   CarProfile(
                       id = entity.id,
                       name = entity.name,
                       tags = entity.tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
                       thumbnailPath = entity.thumbnailPath,
                       createdAt = entity.createdAt,
                       updatedAt = entity.updatedAt
                   )
               }
           }
       }

       override suspend fun createProfile(profile: CarProfile): Result<Long> {
           return try {
               val normalizedTags = profile.tags.map { it.trim().lowercase() }.filter { it.isNotEmpty() }.distinct()
               val now = System.currentTimeMillis()
               val entity = profile.copy(tags = normalizedTags, updatedAt = now).toEntity()
               val id = carProfileDao.insert(entity)
               Result.success(id)
           } catch (e: Exception) {
               Result.failure(e)
           }
       }

       override suspend fun updateProfile(profile: CarProfile): Result<Unit> {
           return try {
               val normalizedTags = profile.tags.map { it.trim().lowercase() }.filter { it.isNotEmpty() }.distinct()
               val now = System.currentTimeMillis()
               val entity = profile.copy(tags = normalizedTags, updatedAt = now).toEntity()
               carProfileDao.update(entity)
               Result.success(Unit)
           } catch (e: Exception) {
               Result.failure(e)
           }
       }

       override suspend fun deleteProfile(id: Long): Result<Unit> {
           return try {
               carProfileDao.deleteById(id)
               Result.success(Unit)
           } catch (e: Exception) {
               Result.failure(e)
           }
       }

       override suspend fun isNameUnique(name: String, excludeId: Long): Boolean {
           return carProfileDao.countByName(name, excludeId) == 0
       }
   }
   ```

5. **Create Repository Module**
   ```kotlin
   // core/database/src/main/java/com/mini4wd/lab/core/database/di/RepositoryModule.kt
   package com.mini4wd.lab.core.database.di

   import com.mini4wd.lab.core.database.repository.CarProfileRepository
   import com.mini4wd.lab.core.database.repository.CarProfileRepositoryImpl
   import dagger.Binds
   import dagger.Module
   import dagger.hilt.InstallIn
   import dagger.hilt.components.SingletonComponent
   import javax.inject.Singleton

   @Module
   @InstallIn(SingletonComponent::class)
   abstract class RepositoryModule {

       @Binds
       @Singleton
       abstract fun bindCarProfileRepository(
           impl: CarProfileRepositoryImpl
       ): CarProfileRepository
   }
   ```

8. **Add Migration Test**
   ```kotlin
   // core/database/src/androidTest/java/com/mini4wd/lab/core/database/MigrationTest.kt
   package com.mini4wd.lab.core.database

   import androidx.room.testing.MigrationTestHelper
   import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
   import androidx.test.platform.app.InstrumentationRegistry
   import com.mini4wd.lab.core.database.migration.MIGRATION_1_2
   import org.junit.Rule
   import org.junit.Test

   class MigrationTest {
       @get:Rule
       val helper = MigrationTestHelper(
           InstrumentationRegistry.getInstrumentation(),
           Mini4WDDatabase::class.java.canonicalName,
           FrameworkSQLiteOpenHelperFactory()
       )

       @Test
       fun migrate1To2_preservesReferenceData() {
           helper.createDatabase("mini4wd_lab.db", 1).apply {
               close()
           }

           helper.runMigrationsAndValidate(
               "mini4wd_lab.db",
               2,
               true,
               MIGRATION_1_2
           )
       }
   }
   ```

---

## Task 1.3: Garage Screen - Profile List UI

### Description
Create the Garage screen showing all car profiles in a grid/list view.

### Prerequisites
- Task 1.2 completed

### Acceptance Criteria
- [ ] Grid view displays all profiles with thumbnails
- [ ] List/Grid toggle available
- [ ] Empty state shown when no profiles exist
- [ ] FAB to add new profile
- [ ] Pull-to-refresh functionality
- [ ] Swipe to delete with confirmation

### Implementation Steps

1. **Create Garage ViewModel**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/garage/GarageViewModel.kt
   package com.mini4wd.lab.feature.profile.ui.garage

   import androidx.lifecycle.ViewModel
   import androidx.lifecycle.viewModelScope
   import com.mini4wd.lab.core.common.model.CarProfile
   import com.mini4wd.lab.core.database.repository.CarProfileRepository
   import dagger.hilt.android.lifecycle.HiltViewModel
   import kotlinx.coroutines.flow.*
   import kotlinx.coroutines.launch
   import javax.inject.Inject

   data class GarageUiState(
       val profiles: List<CarProfile> = emptyList(),
       val isLoading: Boolean = true,
       val searchQuery: String = "",
       val viewMode: ViewMode = ViewMode.GRID,
       val error: String? = null
   )

   enum class ViewMode { GRID, LIST }

   sealed class GarageEvent {
       data class DeleteProfile(val id: Long) : GarageEvent()
       data class NavigateToProfile(val id: Long) : GarageEvent()
       data object NavigateToCreateProfile : GarageEvent()
   }

   @HiltViewModel
   class GarageViewModel @Inject constructor(
       private val repository: CarProfileRepository
   ) : ViewModel() {

       private val _uiState = MutableStateFlow(GarageUiState())
       val uiState: StateFlow<GarageUiState> = _uiState.asStateFlow()

       private val _events = MutableSharedFlow<GarageEvent>()
       val events: SharedFlow<GarageEvent> = _events.asSharedFlow()

       private val searchQuery = MutableStateFlow("")

       init {
           observeProfiles()
       }

       private fun observeProfiles() {
           viewModelScope.launch {
               combine(
                   searchQuery,
                   searchQuery.flatMapLatest { query ->
                       if (query.isBlank()) {
                           repository.getAllProfiles()
                       } else {
                           repository.searchProfiles(query)
                       }
                   }
               ) { query, profiles ->
                   _uiState.value.copy(
                       profiles = profiles,
                       searchQuery = query,
                       isLoading = false
                   )
               }.catch { e ->
                   emit(_uiState.value.copy(error = e.message, isLoading = false))
               }.collect { state ->
                   _uiState.value = state
               }
           }
       }

       fun onSearchQueryChange(query: String) {
           searchQuery.value = query
       }

       fun onViewModeToggle() {
           _uiState.value = _uiState.value.copy(
               viewMode = if (_uiState.value.viewMode == ViewMode.GRID) ViewMode.LIST else ViewMode.GRID
           )
       }

       fun onDeleteProfile(id: Long) {
           viewModelScope.launch {
               repository.deleteProfile(id)
           }
       }

       fun onProfileClick(id: Long) {
           viewModelScope.launch {
               _events.emit(GarageEvent.NavigateToProfile(id))
           }
       }

       fun onAddProfileClick() {
           viewModelScope.launch {
               _events.emit(GarageEvent.NavigateToCreateProfile)
           }
       }

       fun clearError() {
           _uiState.value = _uiState.value.copy(error = null)
       }
   }
   ```

2. **Create Profile Card Component**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/components/ProfileCard.kt
   package com.mini4wd.lab.feature.profile.ui.components

   import androidx.compose.foundation.background
   import androidx.compose.foundation.clickable
   import androidx.compose.foundation.layout.*
   import androidx.compose.foundation.shape.RoundedCornerShape
   import androidx.compose.material.icons.Icons
   import androidx.compose.material.icons.filled.DirectionsCar
   import androidx.compose.material3.*
   import androidx.compose.runtime.Composable
   import androidx.compose.ui.Alignment
   import androidx.compose.ui.Modifier
   import androidx.compose.ui.draw.clip
   import androidx.compose.ui.layout.ContentScale
   import androidx.compose.ui.text.style.TextOverflow
   import androidx.compose.ui.unit.dp
   import coil.compose.AsyncImage
   import com.mini4wd.lab.core.common.model.CarProfile

   @Composable
   fun ProfileGridCard(
       profile: CarProfile,
       onClick: () -> Unit,
       modifier: Modifier = Modifier
   ) {
       Card(
           modifier = modifier
               .fillMaxWidth()
               .clickable(onClick = onClick),
           shape = RoundedCornerShape(12.dp),
           elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
       ) {
           Column {
               // Thumbnail
               Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(120.dp)
                       .background(MaterialTheme.colorScheme.surfaceVariant),
                   contentAlignment = Alignment.Center
               ) {
                   if (profile.thumbnailPath != null) {
                       AsyncImage(
                           model = profile.thumbnailPath,
                           contentDescription = profile.name,
                           modifier = Modifier.fillMaxSize(),
                           contentScale = ContentScale.Crop
                       )
                   } else {
                       Icon(
                           imageVector = Icons.Default.DirectionsCar,
                           contentDescription = null,
                           modifier = Modifier.size(48.dp),
                           tint = MaterialTheme.colorScheme.onSurfaceVariant
                       )
                   }
               }

               // Info
               Column(
                   modifier = Modifier.padding(12.dp)
               ) {
                   Text(
                       text = profile.name,
                       style = MaterialTheme.typography.titleMedium,
                       maxLines = 1,
                       overflow = TextOverflow.Ellipsis
                   )

                   profile.chassis?.let {
                       Text(
                           text = it.code,
                           style = MaterialTheme.typography.bodySmall,
                           color = MaterialTheme.colorScheme.onSurfaceVariant
                       )
                   }

                   if (profile.tags.isNotEmpty()) {
                       Row(
                           modifier = Modifier.padding(top = 4.dp),
                           horizontalArrangement = Arrangement.spacedBy(4.dp)
                       ) {
                           profile.tags.take(2).forEach { tag ->
                               SuggestionChip(
                                   onClick = {},
                                   label = {
                                       Text(
                                           text = tag,
                                           style = MaterialTheme.typography.labelSmall
                                       )
                                   },
                                   modifier = Modifier.height(24.dp)
                               )
                           }
                       }
                   }
               }
           }
       }
   }

   @Composable
   fun ProfileListItem(
       profile: CarProfile,
       onClick: () -> Unit,
       modifier: Modifier = Modifier
   ) {
       ListItem(
           modifier = modifier.clickable(onClick = onClick),
           headlineContent = {
               Text(
                   text = profile.name,
                   maxLines = 1,
                   overflow = TextOverflow.Ellipsis
               )
           },
           supportingContent = {
               profile.chassis?.let {
                   Text(text = "${it.code} Chassis")
               }
           },
           leadingContent = {
               Box(
                   modifier = Modifier
                       .size(56.dp)
                       .clip(RoundedCornerShape(8.dp))
                       .background(MaterialTheme.colorScheme.surfaceVariant),
                   contentAlignment = Alignment.Center
               ) {
                   if (profile.thumbnailPath != null) {
                       AsyncImage(
                           model = profile.thumbnailPath,
                           contentDescription = profile.name,
                           modifier = Modifier.fillMaxSize(),
                           contentScale = ContentScale.Crop
                       )
                   } else {
                       Icon(
                           imageVector = Icons.Default.DirectionsCar,
                           contentDescription = null,
                           tint = MaterialTheme.colorScheme.onSurfaceVariant
                       )
                   }
               }
           }
       )
   }
   ```

3. **Create Garage Screen**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/garage/GarageScreen.kt
   package com.mini4wd.lab.feature.profile.ui.garage

   import androidx.compose.foundation.layout.*
   import androidx.compose.foundation.lazy.LazyColumn
   import androidx.compose.foundation.lazy.grid.GridCells
   import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
   import androidx.compose.foundation.lazy.grid.items
   import androidx.compose.foundation.lazy.items
   import androidx.compose.material.icons.Icons
   import androidx.compose.material.icons.filled.Add
   import androidx.compose.material.icons.filled.GridView
   import androidx.compose.material.icons.filled.Search
   import androidx.compose.material.icons.filled.ViewList
   import androidx.compose.material3.*
   import androidx.compose.runtime.*
   import androidx.compose.ui.Alignment
   import androidx.compose.ui.Modifier
   import androidx.compose.ui.res.stringResource
   import androidx.compose.ui.unit.dp
   import androidx.hilt.navigation.compose.hiltViewModel
   import com.mini4wd.lab.feature.profile.R
   import com.mini4wd.lab.feature.profile.ui.components.ProfileGridCard
   import com.mini4wd.lab.feature.profile.ui.components.ProfileListItem

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun GarageScreen(
       onNavigateToProfile: (Long) -> Unit,
       onNavigateToCreateProfile: () -> Unit,
       viewModel: GarageViewModel = hiltViewModel()
   ) {
       val uiState by viewModel.uiState.collectAsState()

       LaunchedEffect(Unit) {
           viewModel.events.collect { event ->
               when (event) {
                   is GarageEvent.NavigateToProfile -> onNavigateToProfile(event.id)
                   is GarageEvent.NavigateToCreateProfile -> onNavigateToCreateProfile()
                   is GarageEvent.DeleteProfile -> { /* Handled internally */ }
               }
           }
       }

       Scaffold(
           topBar = {
               TopAppBar(
                   title = { Text("Garage") },
                   actions = {
                       IconButton(onClick = viewModel::onViewModeToggle) {
                           Icon(
                               imageVector = if (uiState.viewMode == ViewMode.GRID)
                                   Icons.Default.ViewList else Icons.Default.GridView,
                               contentDescription = "Toggle view mode"
                           )
                       }
                   }
               )
           },
           floatingActionButton = {
               FloatingActionButton(
                   onClick = viewModel::onAddProfileClick
               ) {
                   Icon(Icons.Default.Add, contentDescription = "Add profile")
               }
           }
       ) { paddingValues ->
           Column(
               modifier = Modifier
                   .fillMaxSize()
                   .padding(paddingValues)
           ) {
               // Search bar
               OutlinedTextField(
                   value = uiState.searchQuery,
                   onValueChange = viewModel::onSearchQueryChange,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 16.dp, vertical = 8.dp),
                   placeholder = { Text("Search profiles...") },
                   leadingIcon = {
                       Icon(Icons.Default.Search, contentDescription = null)
                   },
                   singleLine = true
               )

               when {
                   uiState.isLoading -> {
                       Box(
                           modifier = Modifier.fillMaxSize(),
                           contentAlignment = Alignment.Center
                       ) {
                           CircularProgressIndicator()
                       }
                   }
                   uiState.profiles.isEmpty() -> {
                       EmptyGarageState(
                           onAddClick = viewModel::onAddProfileClick,
                           modifier = Modifier.fillMaxSize()
                       )
                   }
                   uiState.viewMode == ViewMode.GRID -> {
                       LazyVerticalGrid(
                           columns = GridCells.Fixed(2),
                           contentPadding = PaddingValues(16.dp),
                           horizontalArrangement = Arrangement.spacedBy(12.dp),
                           verticalArrangement = Arrangement.spacedBy(12.dp),
                           modifier = Modifier.fillMaxSize()
                       ) {
                           items(
                               items = uiState.profiles,
                               key = { it.id }
                           ) { profile ->
                               ProfileGridCard(
                                   profile = profile,
                                   onClick = { viewModel.onProfileClick(profile.id) }
                               )
                           }
                       }
                   }
                   else -> {
                       LazyColumn(
                           modifier = Modifier.fillMaxSize()
                       ) {
                           items(
                               items = uiState.profiles,
                               key = { it.id }
                           ) { profile ->
                               ProfileListItem(
                                   profile = profile,
                                   onClick = { viewModel.onProfileClick(profile.id) }
                               )
                               HorizontalDivider()
                           }
                       }
                   }
               }
           }
       }

       // Error snackbar
       uiState.error?.let { error ->
           LaunchedEffect(error) {
               // Show snackbar
               viewModel.clearError()
           }
       }
   }

   @Composable
   private fun EmptyGarageState(
       onAddClick: () -> Unit,
       modifier: Modifier = Modifier
   ) {
       Column(
           modifier = modifier,
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           Text(
               text = "No cars in your garage",
               style = MaterialTheme.typography.headlineSmall
           )
           Spacer(modifier = Modifier.height(8.dp))
           Text(
               text = "Add your first Mini 4WD car to get started",
               style = MaterialTheme.typography.bodyMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant
           )
           Spacer(modifier = Modifier.height(24.dp))
           Button(onClick = onAddClick) {
               Icon(Icons.Default.Add, contentDescription = null)
               Spacer(modifier = Modifier.width(8.dp))
               Text("Add Car")
           }
       }
   }
   ```

---

## Task 1.4: Profile Create/Edit Screen

### Description
Create the screen for adding and editing car profiles with all configuration fields.

### Prerequisites
- Task 1.3 completed

### Acceptance Criteria
- [ ] Form with all profile fields organized in sections
- [ ] Dropdown selections for chassis, motor, gear ratio (filtered by shaft type)
- [ ] Validation for required fields (name is required and unique)
- [ ] Roller/Damper/Brake configuration captured (text/JSON placeholders acceptable for v1)
- [ ] Save/Cancel actions
- [ ] Reuses same screen for create and edit modes

### Implementation Steps

1. **Create Profile Edit ViewModel**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/edit/ProfileEditViewModel.kt
   package com.mini4wd.lab.feature.profile.ui.edit

   import androidx.lifecycle.SavedStateHandle
   import androidx.lifecycle.ViewModel
   import androidx.lifecycle.viewModelScope
   import com.mini4wd.lab.core.common.model.*
   import com.mini4wd.lab.core.database.dao.ChassisRefDao
   import com.mini4wd.lab.core.database.dao.GearRatioRefDao
   import com.mini4wd.lab.core.database.dao.MotorRefDao
   import com.mini4wd.lab.core.database.entity.ChassisRefEntity
   import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
   import com.mini4wd.lab.core.database.entity.MotorRefEntity
   import com.mini4wd.lab.core.database.repository.CarProfileRepository
   import dagger.hilt.android.lifecycle.HiltViewModel
   import kotlinx.coroutines.flow.*
   import kotlinx.coroutines.launch
   import javax.inject.Inject

   data class ProfileEditUiState(
       val isEditMode: Boolean = false,
       val profileId: Long? = null,
       val name: String = "",
       val nameError: String? = null,
       val selectedChassis: ChassisRefEntity? = null,
       val selectedMotor: MotorRefEntity? = null,
       val selectedGearRatio: GearRatioRefEntity? = null,
       val motorBreakInStatus: Boolean = false,
       val motorRunCount: Int = 0,
       val customModNotes: String = "",
       val tireFrontType: String = "",
       val tireFrontMaterial: String = "",
       val tireFrontDiameter: String = "",
       val tireRearType: String = "",
       val tireRearMaterial: String = "",
       val tireRearDiameter: String = "",
       val wheelType: String = "",
       val wheelMaterial: String = "",
       val bodyType: String = "",
       val totalWeight: String = "",
       val batteryType: String = "",
       val batteryBrand: String = "",
       val tags: String = "",
       val chassisList: List<ChassisRefEntity> = emptyList(),
       val motorList: List<MotorRefEntity> = emptyList(),
       val gearRatioList: List<GearRatioRefEntity> = emptyList(),
       val isLoading: Boolean = true,
       val isSaving: Boolean = false,
       val saveSuccess: Boolean = false,
       val error: String? = null
   )

   @HiltViewModel
   class ProfileEditViewModel @Inject constructor(
       savedStateHandle: SavedStateHandle,
       private val repository: CarProfileRepository,
       private val chassisRefDao: ChassisRefDao,
       private val motorRefDao: MotorRefDao,
       private val gearRatioRefDao: GearRatioRefDao
   ) : ViewModel() {

       private val profileId: Long? = savedStateHandle.get<Long>("profileId")?.takeIf { it > 0 }

       private val _uiState = MutableStateFlow(ProfileEditUiState(
           isEditMode = profileId != null,
           profileId = profileId
       ))
       val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

       init {
           loadReferenceData()
           profileId?.let { loadProfile(it) }
       }

       private fun loadReferenceData() {
           viewModelScope.launch {
               combine(
                   chassisRefDao.getAllChassis(),
                   motorRefDao.getAllMotors(),
                   gearRatioRefDao.getAllGearRatios()
               ) { chassis, motors, gearRatios ->
                   Triple(chassis, motors, gearRatios)
               }.collect { (chassis, motors, gearRatios) ->
                   _uiState.value = _uiState.value.copy(
                       chassisList = chassis,
                       motorList = motors,
                       gearRatioList = gearRatios,
                       isLoading = profileId != null // Still loading if editing
                   )
               }
           }
       }

       private fun loadProfile(id: Long) {
           viewModelScope.launch {
               repository.getProfileById(id).first()?.let { profile ->
                   _uiState.value = _uiState.value.copy(
                       name = profile.name,
                       selectedChassis = _uiState.value.chassisList.find { it.id == profile.chassis?.id },
                       selectedMotor = _uiState.value.motorList.find { it.id == profile.motor?.id },
                       selectedGearRatio = _uiState.value.gearRatioList.find { it.id == profile.gearRatio?.id },
                       motorBreakInStatus = profile.motor?.breakInStatus ?: false,
                       motorRunCount = profile.motor?.runCount ?: 0,
                       customModNotes = profile.customModNotes ?: "",
                       tireFrontType = profile.tires.frontType ?: "",
                       tireFrontMaterial = profile.tires.frontMaterial ?: "",
                       tireFrontDiameter = profile.tires.frontDiameterMm?.toString() ?: "",
                       tireRearType = profile.tires.rearType ?: "",
                       tireRearMaterial = profile.tires.rearMaterial ?: "",
                       tireRearDiameter = profile.tires.rearDiameterMm?.toString() ?: "",
                       wheelType = profile.wheels?.type ?: "",
                       wheelMaterial = profile.wheels?.material ?: "",
                       bodyType = profile.bodyType ?: "",
                       totalWeight = profile.totalWeightG?.toString() ?: "",
                       batteryType = profile.batteryType ?: "",
                       batteryBrand = profile.batteryBrand ?: "",
                       tags = profile.tags.joinToString(", "),
                       isLoading = false
                   )
               }
           }
       }

       // Get motors filtered by selected chassis shaft type
       fun getFilteredMotors(): List<MotorRefEntity> {
           val shaftType = _uiState.value.selectedChassis?.shaftType ?: return _uiState.value.motorList
           return _uiState.value.motorList.filter { it.shaftType == shaftType }
       }

       // Get gear ratios filtered by selected chassis shaft type
       fun getFilteredGearRatios(): List<GearRatioRefEntity> {
           val shaftType = _uiState.value.selectedChassis?.shaftType ?: return _uiState.value.gearRatioList
           return _uiState.value.gearRatioList.filter { it.shaftType == shaftType }
       }

       fun onNameChange(name: String) {
           _uiState.value = _uiState.value.copy(name = name, nameError = null)
       }

       fun onChassisSelect(chassis: ChassisRefEntity?) {
           val currentMotor = _uiState.value.selectedMotor
           val currentGearRatio = _uiState.value.selectedGearRatio

           // Clear motor and gear ratio if shaft type changes
           val clearSelections = chassis?.shaftType != _uiState.value.selectedChassis?.shaftType

           _uiState.value = _uiState.value.copy(
               selectedChassis = chassis,
               selectedMotor = if (clearSelections) null else currentMotor,
               selectedGearRatio = if (clearSelections) null else currentGearRatio
           )
       }

       fun onMotorSelect(motor: MotorRefEntity?) {
           _uiState.value = _uiState.value.copy(selectedMotor = motor)
       }

       fun onGearRatioSelect(gearRatio: GearRatioRefEntity?) {
           _uiState.value = _uiState.value.copy(selectedGearRatio = gearRatio)
       }

       fun onMotorBreakInStatusChange(status: Boolean) {
           _uiState.value = _uiState.value.copy(motorBreakInStatus = status)
       }

       fun onMotorRunCountChange(count: String) {
           _uiState.value = _uiState.value.copy(motorRunCount = count.toIntOrNull() ?: 0)
       }

       fun onCustomModNotesChange(notes: String) {
           _uiState.value = _uiState.value.copy(customModNotes = notes)
       }

       fun onTireFrontTypeChange(type: String) {
           _uiState.value = _uiState.value.copy(tireFrontType = type)
       }

       fun onTireFrontMaterialChange(material: String) {
           _uiState.value = _uiState.value.copy(tireFrontMaterial = material)
       }

       fun onTireFrontDiameterChange(diameter: String) {
           _uiState.value = _uiState.value.copy(tireFrontDiameter = diameter)
       }

       fun onTireRearTypeChange(type: String) {
           _uiState.value = _uiState.value.copy(tireRearType = type)
       }

       fun onTireRearMaterialChange(material: String) {
           _uiState.value = _uiState.value.copy(tireRearMaterial = material)
       }

       fun onTireRearDiameterChange(diameter: String) {
           _uiState.value = _uiState.value.copy(tireRearDiameter = diameter)
       }

       fun onWheelTypeChange(type: String) {
           _uiState.value = _uiState.value.copy(wheelType = type)
       }

       fun onWheelMaterialChange(material: String) {
           _uiState.value = _uiState.value.copy(wheelMaterial = material)
       }

       fun onBodyTypeChange(type: String) {
           _uiState.value = _uiState.value.copy(bodyType = type)
       }

       fun onTotalWeightChange(weight: String) {
           _uiState.value = _uiState.value.copy(totalWeight = weight)
       }

       fun onBatteryTypeChange(type: String) {
           _uiState.value = _uiState.value.copy(batteryType = type)
       }

       fun onBatteryBrandChange(brand: String) {
           _uiState.value = _uiState.value.copy(batteryBrand = brand)
       }

       fun onTagsChange(tags: String) {
           _uiState.value = _uiState.value.copy(tags = tags)
       }

       fun save() {
           viewModelScope.launch {
               val state = _uiState.value

               // Validate name
               if (state.name.isBlank()) {
                   _uiState.value = state.copy(nameError = "Name is required")
                   return@launch
               }

               // Check name uniqueness
               val isUnique = repository.isNameUnique(state.name, state.profileId ?: 0)
               if (!isUnique) {
                   _uiState.value = state.copy(nameError = "A car with this name already exists")
                   return@launch
               }

               _uiState.value = state.copy(isSaving = true)

               val profile = CarProfile(
                   id = state.profileId ?: 0,
                   name = state.name,
                   chassis = state.selectedChassis?.let {
                       ChassisInfo(it.id, it.code, it.name, it.shaftType, it.motorPosition)
                   },
                   motor = state.selectedMotor?.let {
                       MotorInfo(
                           id = it.id,
                           name = it.name,
                           shaftType = it.shaftType,
                           category = it.category,
                           rpmMin = it.rpmMin,
                           rpmMax = it.rpmMax,
                           breakInStatus = state.motorBreakInStatus,
                           runCount = state.motorRunCount
                       )
                   },
                   gearRatio = state.selectedGearRatio?.let {
                       GearRatioInfo(it.id, it.ratio, it.ratioValue, it.shaftType, it.gear1Color, it.gear2Color)
                   },
                   tires = TireConfig(
                       frontType = state.tireFrontType.takeIf { it.isNotBlank() },
                       frontMaterial = state.tireFrontMaterial.takeIf { it.isNotBlank() },
                       frontDiameterMm = state.tireFrontDiameter.toFloatOrNull(),
                       rearType = state.tireRearType.takeIf { it.isNotBlank() },
                       rearMaterial = state.tireRearMaterial.takeIf { it.isNotBlank() },
                       rearDiameterMm = state.tireRearDiameter.toFloatOrNull()
                   ),
                   wheels = if (state.wheelType.isNotBlank() || state.wheelMaterial.isNotBlank()) {
                       WheelConfig(
                           type = state.wheelType.takeIf { it.isNotBlank() },
                           material = state.wheelMaterial.takeIf { it.isNotBlank() }
                       )
                   } else null,
                   customModNotes = state.customModNotes.takeIf { it.isNotBlank() },
                   bodyType = state.bodyType.takeIf { it.isNotBlank() },
                   totalWeightG = state.totalWeight.toFloatOrNull(),
                   batteryType = state.batteryType.takeIf { it.isNotBlank() },
                   batteryBrand = state.batteryBrand.takeIf { it.isNotBlank() },
                   tags = state.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
               )

               val result = if (state.isEditMode) {
                   repository.updateProfile(profile)
               } else {
                   repository.createProfile(profile).map { }
               }

               result.fold(
                   onSuccess = {
                       _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true)
                   },
                   onFailure = { e ->
                       val nameError = if (e is android.database.sqlite.SQLiteConstraintException) {
                           "A car with this name already exists"
                       } else null
                       _uiState.value = _uiState.value.copy(
                           isSaving = false,
                           error = e.message,
                           nameError = nameError
                       )
                   }
               )
           }
       }

       fun clearError() {
           _uiState.value = _uiState.value.copy(error = null)
       }
   }
   ```

2. **Create Profile Edit Screen**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/edit/ProfileEditScreen.kt
   package com.mini4wd.lab.feature.profile.ui.edit

   import androidx.compose.foundation.layout.*
   import androidx.compose.foundation.rememberScrollState
   import androidx.compose.foundation.verticalScroll
   import androidx.compose.material.icons.Icons
   import androidx.compose.material.icons.filled.ArrowBack
   import androidx.compose.material.icons.filled.Check
   import androidx.compose.material3.*
   import androidx.compose.runtime.*
   import androidx.compose.ui.Modifier
   import androidx.compose.ui.unit.dp
   import androidx.hilt.navigation.compose.hiltViewModel

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun ProfileEditScreen(
       onNavigateBack: () -> Unit,
       viewModel: ProfileEditViewModel = hiltViewModel()
   ) {
       val uiState by viewModel.uiState.collectAsState()

       LaunchedEffect(uiState.saveSuccess) {
           if (uiState.saveSuccess) {
               onNavigateBack()
           }
       }

       Scaffold(
           topBar = {
               TopAppBar(
                   title = {
                       Text(if (uiState.isEditMode) "Edit Car" else "New Car")
                   },
                   navigationIcon = {
                       IconButton(onClick = onNavigateBack) {
                           Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                       }
                   },
                   actions = {
                       IconButton(
                           onClick = viewModel::save,
                           enabled = !uiState.isSaving
                       ) {
                           if (uiState.isSaving) {
                               CircularProgressIndicator(
                                   modifier = Modifier.size(24.dp),
                                   strokeWidth = 2.dp
                               )
                           } else {
                               Icon(Icons.Default.Check, contentDescription = "Save")
                           }
                       }
                   }
               )
           }
       ) { paddingValues ->
           if (uiState.isLoading) {
               Box(
                   modifier = Modifier
                       .fillMaxSize()
                       .padding(paddingValues),
                   contentAlignment = androidx.compose.ui.Alignment.Center
               ) {
                   CircularProgressIndicator()
               }
           } else {
               Column(
                   modifier = Modifier
                       .fillMaxSize()
                       .padding(paddingValues)
                       .verticalScroll(rememberScrollState())
                       .padding(16.dp),
                   verticalArrangement = Arrangement.spacedBy(16.dp)
               ) {
                   // Basic Info Section
                   SectionHeader("Basic Info")

                   OutlinedTextField(
                       value = uiState.name,
                       onValueChange = viewModel::onNameChange,
                       label = { Text("Car Name *") },
                       isError = uiState.nameError != null,
                       supportingText = uiState.nameError?.let { { Text(it) } },
                       modifier = Modifier.fillMaxWidth(),
                       singleLine = true
                   )

                   // Chassis Selection
                   ChassisDropdown(
                       selected = uiState.selectedChassis,
                       options = uiState.chassisList,
                       onSelect = viewModel::onChassisSelect
                   )

                   // Motor Section
                   SectionHeader("Motor")

                   MotorDropdown(
                       selected = uiState.selectedMotor,
                       options = viewModel.getFilteredMotors(),
                       onSelect = viewModel::onMotorSelect,
                       enabled = uiState.selectedChassis != null
                   )

                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.spacedBy(16.dp)
                   ) {
                       Row(
                           modifier = Modifier.weight(1f),
                           verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                       ) {
                           Checkbox(
                               checked = uiState.motorBreakInStatus,
                               onCheckedChange = viewModel::onMotorBreakInStatusChange
                           )
                           Text("Break-in Complete")
                       }

                       OutlinedTextField(
                           value = if (uiState.motorRunCount > 0) uiState.motorRunCount.toString() else "",
                           onValueChange = viewModel::onMotorRunCountChange,
                           label = { Text("Run Count") },
                           modifier = Modifier.weight(1f),
                           singleLine = true
                       )
                   }

                   // Gear Ratio Section
                   SectionHeader("Gear Ratio")

                   GearRatioDropdown(
                       selected = uiState.selectedGearRatio,
                       options = viewModel.getFilteredGearRatios(),
                       onSelect = viewModel::onGearRatioSelect,
                       enabled = uiState.selectedChassis != null
                   )

                   // Tire Section
                   SectionHeader("Tires")

                   Text("Front Tires", style = MaterialTheme.typography.labelMedium)
                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       OutlinedTextField(
                           value = uiState.tireFrontType,
                           onValueChange = viewModel::onTireFrontTypeChange,
                           label = { Text("Type") },
                           modifier = Modifier.weight(1f),
                           singleLine = true
                       )
                       OutlinedTextField(
                           value = uiState.tireFrontMaterial,
                           onValueChange = viewModel::onTireFrontMaterialChange,
                           label = { Text("Material") },
                           modifier = Modifier.weight(1f),
                           singleLine = true
                       )
                       OutlinedTextField(
                           value = uiState.tireFrontDiameter,
                           onValueChange = viewModel::onTireFrontDiameterChange,
                           label = { Text("Ø mm") },
                           modifier = Modifier.weight(0.7f),
                           singleLine = true
                       )
                   }

                   Text("Rear Tires", style = MaterialTheme.typography.labelMedium)
                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       OutlinedTextField(
                           value = uiState.tireRearType,
                           onValueChange = viewModel::onTireRearTypeChange,
                           label = { Text("Type") },
                           modifier = Modifier.weight(1f),
                           singleLine = true
                       )
                       OutlinedTextField(
                           value = uiState.tireRearMaterial,
                           onValueChange = viewModel::onTireRearMaterialChange,
                           label = { Text("Material") },
                           modifier = Modifier.weight(1f),
                           singleLine = true
                       )
                       OutlinedTextField(
                           value = uiState.tireRearDiameter,
                           onValueChange = viewModel::onTireRearDiameterChange,
                           label = { Text("Ø mm") },
                           modifier = Modifier.weight(0.7f),
                           singleLine = true
                       )
                   }

                   // Additional Config Section
                   SectionHeader("Additional Configuration")

                   OutlinedTextField(
                       value = uiState.bodyType,
                       onValueChange = viewModel::onBodyTypeChange,
                       label = { Text("Body Shell") },
                       modifier = Modifier.fillMaxWidth(),
                       singleLine = true
                   )

                   OutlinedTextField(
                       value = uiState.totalWeight,
                       onValueChange = viewModel::onTotalWeightChange,
                       label = { Text("Total Weight (g)") },
                       modifier = Modifier.fillMaxWidth(),
                       singleLine = true
                   )

                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       OutlinedTextField(
                           value = uiState.batteryType,
                           onValueChange = viewModel::onBatteryTypeChange,
                           label = { Text("Battery Type") },
                           modifier = Modifier.weight(1f),
                           singleLine = true
                       )
                       OutlinedTextField(
                           value = uiState.batteryBrand,
                           onValueChange = viewModel::onBatteryBrandChange,
                           label = { Text("Battery Brand") },
                           modifier = Modifier.weight(1f),
                           singleLine = true
                       )
                   }

                   OutlinedTextField(
                       value = uiState.customModNotes,
                       onValueChange = viewModel::onCustomModNotesChange,
                       label = { Text("Custom Modifications") },
                       modifier = Modifier.fillMaxWidth(),
                       minLines = 3,
                       maxLines = 5
                   )

                   OutlinedTextField(
                       value = uiState.tags,
                       onValueChange = viewModel::onTagsChange,
                       label = { Text("Tags (comma-separated)") },
                       placeholder = { Text("e.g., race, speed, experimental") },
                       modifier = Modifier.fillMaxWidth(),
                       singleLine = true
                   )

                   Spacer(modifier = Modifier.height(32.dp))
               }
           }
       }
   }

   @Composable
   private fun SectionHeader(title: String) {
       Text(
           text = title,
           style = MaterialTheme.typography.titleMedium,
           color = MaterialTheme.colorScheme.primary
       )
   }

   // Dropdown composables (ChassisDropdown, MotorDropdown, GearRatioDropdown)
   // Implementation similar to standard ExposedDropdownMenuBox pattern
   ```

3. **Create Dropdown Components**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/components/Dropdowns.kt
   package com.mini4wd.lab.feature.profile.ui.components

   import androidx.compose.foundation.layout.fillMaxWidth
   import androidx.compose.material3.*
   import androidx.compose.runtime.*
   import androidx.compose.ui.Modifier
   import com.mini4wd.lab.core.database.entity.ChassisRefEntity
   import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
   import com.mini4wd.lab.core.database.entity.MotorRefEntity

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun ChassisDropdown(
       selected: ChassisRefEntity?,
       options: List<ChassisRefEntity>,
       onSelect: (ChassisRefEntity?) -> Unit,
       modifier: Modifier = Modifier
   ) {
       var expanded by remember { mutableStateOf(false) }

       ExposedDropdownMenuBox(
           expanded = expanded,
           onExpandedChange = { expanded = it },
           modifier = modifier
       ) {
           OutlinedTextField(
               value = selected?.let { "${it.code} - ${it.name}" } ?: "",
               onValueChange = {},
               readOnly = true,
               label = { Text("Chassis") },
               trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
               modifier = Modifier
                   .fillMaxWidth()
                   .menuAnchor()
           )

           ExposedDropdownMenu(
               expanded = expanded,
               onDismissRequest = { expanded = false }
           ) {
               DropdownMenuItem(
                   text = { Text("None") },
                   onClick = {
                       onSelect(null)
                       expanded = false
                   }
               )
               options.forEach { chassis ->
                   DropdownMenuItem(
                       text = {
                           Text("${chassis.code} - ${chassis.name} (${chassis.shaftType})")
                       },
                       onClick = {
                           onSelect(chassis)
                           expanded = false
                       }
                   )
               }
           }
       }
   }

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun MotorDropdown(
       selected: MotorRefEntity?,
       options: List<MotorRefEntity>,
       onSelect: (MotorRefEntity?) -> Unit,
       enabled: Boolean = true,
       modifier: Modifier = Modifier
   ) {
       var expanded by remember { mutableStateOf(false) }

       ExposedDropdownMenuBox(
           expanded = expanded && enabled,
           onExpandedChange = { if (enabled) expanded = it },
           modifier = modifier
       ) {
           OutlinedTextField(
               value = selected?.name ?: "",
               onValueChange = {},
               readOnly = true,
               enabled = enabled,
               label = { Text("Motor") },
               supportingText = selected?.let {
                   { Text("${it.rpmMin.formatRpm()} - ${it.rpmMax.formatRpm()} RPM") }
               },
               trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
               modifier = Modifier
                   .fillMaxWidth()
                   .menuAnchor()
           )

           ExposedDropdownMenu(
               expanded = expanded,
               onDismissRequest = { expanded = false }
           ) {
               DropdownMenuItem(
                   text = { Text("None") },
                   onClick = {
                       onSelect(null)
                       expanded = false
                   }
               )
               options.forEach { motor ->
                   DropdownMenuItem(
                       text = {
                           Text("${motor.name} (${motor.rpmMin.formatRpm()}-${motor.rpmMax.formatRpm()})")
                       },
                       onClick = {
                           onSelect(motor)
                           expanded = false
                       }
                   )
               }
           }
       }
   }

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun GearRatioDropdown(
       selected: GearRatioRefEntity?,
       options: List<GearRatioRefEntity>,
       onSelect: (GearRatioRefEntity?) -> Unit,
       enabled: Boolean = true,
       modifier: Modifier = Modifier
   ) {
       var expanded by remember { mutableStateOf(false) }

       ExposedDropdownMenuBox(
           expanded = expanded && enabled,
           onExpandedChange = { if (enabled) expanded = it },
           modifier = modifier
       ) {
           OutlinedTextField(
               value = selected?.ratio ?: "",
               onValueChange = {},
               readOnly = true,
               enabled = enabled,
               label = { Text("Gear Ratio") },
               supportingText = selected?.let {
                   { Text("${it.gear1Color} / ${it.gear2Color}") }
               },
               trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
               modifier = Modifier
                   .fillMaxWidth()
                   .menuAnchor()
           )

           ExposedDropdownMenu(
               expanded = expanded,
               onDismissRequest = { expanded = false }
           ) {
               DropdownMenuItem(
                   text = { Text("None") },
                   onClick = {
                       onSelect(null)
                       expanded = false
                   }
               )
               options.forEach { gearRatio ->
                   DropdownMenuItem(
                       text = {
                           Text("${gearRatio.ratio} (${gearRatio.gear1Color}/${gearRatio.gear2Color})")
                       },
                       onClick = {
                           onSelect(gearRatio)
                           expanded = false
                       }
                   )
               }
           }
       }
   }

   private fun Int.formatRpm(): String = "%,d".format(this)
   ```

---

## Task 1.5: Profile Detail Screen

### Description
Create the profile detail screen showing all configuration information.

### Prerequisites
- Task 1.4 completed

### Acceptance Criteria
- [ ] Displays all profile information in organized sections
- [ ] Visual gear ratio indicator with colors
- [ ] Edit button navigates to edit screen
- [ ] Delete with confirmation dialog
- [ ] Quick-link buttons to Timer and RPM screens (placeholders for now)

### Implementation Steps

1. **Create Profile Detail ViewModel**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/detail/ProfileDetailViewModel.kt
   package com.mini4wd.lab.feature.profile.ui.detail

   import androidx.lifecycle.SavedStateHandle
   import androidx.lifecycle.ViewModel
   import androidx.lifecycle.viewModelScope
   import com.mini4wd.lab.core.common.model.CarProfile
   import com.mini4wd.lab.core.database.repository.CarProfileRepository
   import dagger.hilt.android.lifecycle.HiltViewModel
   import kotlinx.coroutines.flow.*
   import kotlinx.coroutines.launch
   import javax.inject.Inject

   data class ProfileDetailUiState(
       val profile: CarProfile? = null,
       val isLoading: Boolean = true,
       val showDeleteDialog: Boolean = false,
       val isDeleted: Boolean = false,
       val error: String? = null
   )

   @HiltViewModel
   class ProfileDetailViewModel @Inject constructor(
       savedStateHandle: SavedStateHandle,
       private val repository: CarProfileRepository
   ) : ViewModel() {

       private val profileId: Long = checkNotNull(savedStateHandle.get<Long>("profileId"))

       private val _uiState = MutableStateFlow(ProfileDetailUiState())
       val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

       init {
           loadProfile()
       }

       private fun loadProfile() {
           viewModelScope.launch {
               repository.getProfileById(profileId)
                   .catch { e ->
                       _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
                   }
                   .collect { profile ->
                       _uiState.value = _uiState.value.copy(profile = profile, isLoading = false)
                   }
           }
       }

       fun showDeleteDialog() {
           _uiState.value = _uiState.value.copy(showDeleteDialog = true)
       }

       fun dismissDeleteDialog() {
           _uiState.value = _uiState.value.copy(showDeleteDialog = false)
       }

       fun deleteProfile() {
           viewModelScope.launch {
               _uiState.value = _uiState.value.copy(showDeleteDialog = false)
               repository.deleteProfile(profileId).fold(
                   onSuccess = {
                       _uiState.value = _uiState.value.copy(isDeleted = true)
                   },
                   onFailure = { e ->
                       _uiState.value = _uiState.value.copy(error = e.message)
                   }
               )
           }
       }
   }
   ```

2. **Create Gear Ratio Visual Component**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/components/GearRatioIndicator.kt
   package com.mini4wd.lab.feature.profile.ui.components

   import androidx.compose.foundation.background
   import androidx.compose.foundation.layout.*
   import androidx.compose.foundation.shape.CircleShape
   import androidx.compose.material3.MaterialTheme
   import androidx.compose.material3.Text
   import androidx.compose.runtime.Composable
   import androidx.compose.ui.Alignment
   import androidx.compose.ui.Modifier
   import androidx.compose.ui.draw.clip
   import androidx.compose.ui.graphics.Color
   import androidx.compose.ui.unit.dp
   import com.mini4wd.lab.core.common.model.GearRatioInfo

   @Composable
   fun GearRatioIndicator(
       gearRatio: GearRatioInfo,
       modifier: Modifier = Modifier
   ) {
       Row(
           modifier = modifier,
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.spacedBy(12.dp)
       ) {
           // Gear 1 (Spur) color circle
           Column(horizontalAlignment = Alignment.CenterHorizontally) {
               Box(
                   modifier = Modifier
                       .size(32.dp)
                       .clip(CircleShape)
                       .background(gearRatio.gear1Color.toGearColor())
               )
               Text(
                   text = "Spur",
                   style = MaterialTheme.typography.labelSmall
               )
           }

           // Ratio display
           Text(
               text = gearRatio.ratio,
               style = MaterialTheme.typography.headlineMedium
           )

           // Gear 2 (Pinion/Counter) color circle
           Column(horizontalAlignment = Alignment.CenterHorizontally) {
               Box(
                   modifier = Modifier
                       .size(24.dp)
                       .clip(CircleShape)
                       .background(gearRatio.gear2Color.toGearColor())
               )
               Text(
                   text = if (gearRatio.shaftType == "double") "Counter" else "Pinion",
                   style = MaterialTheme.typography.labelSmall
               )
           }
       }
   }

   private fun String.toGearColor(): Color {
       return when (this.lowercase()) {
           "yellow" -> Color(0xFFFFEB3B)
           "light blue" -> Color(0xFF03A9F4)
           "green" -> Color(0xFF4CAF50)
           "light green" -> Color(0xFF8BC34A)
           "light brown" -> Color(0xFFBCAAA4)
           "black" -> Color(0xFF212121)
           "red" -> Color(0xFFF44336)
           "blue" -> Color(0xFF2196F3)
           "pink" -> Color(0xFFE91E63)
           "orange" -> Color(0xFFFF9800)
           else -> Color.Gray
       }
   }
   ```

3. **Create Profile Detail Screen**
   ```kotlin
   // feature/profile/src/main/java/com/mini4wd/lab/feature/profile/ui/detail/ProfileDetailScreen.kt
   package com.mini4wd.lab.feature.profile.ui.detail

   import androidx.compose.foundation.layout.*
   import androidx.compose.foundation.rememberScrollState
   import androidx.compose.foundation.verticalScroll
   import androidx.compose.material.icons.Icons
   import androidx.compose.material.icons.filled.*
   import androidx.compose.material3.*
   import androidx.compose.runtime.*
   import androidx.compose.ui.Alignment
   import androidx.compose.ui.Modifier
   import androidx.compose.ui.unit.dp
   import androidx.hilt.navigation.compose.hiltViewModel
   import com.mini4wd.lab.feature.profile.ui.components.GearRatioIndicator

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun ProfileDetailScreen(
       onNavigateBack: () -> Unit,
       onNavigateToEdit: (Long) -> Unit,
       onNavigateToTimer: (Long) -> Unit,
       onNavigateToRpm: (Long) -> Unit,
       viewModel: ProfileDetailViewModel = hiltViewModel()
   ) {
       val uiState by viewModel.uiState.collectAsState()

       LaunchedEffect(uiState.isDeleted) {
           if (uiState.isDeleted) {
               onNavigateBack()
           }
       }

       // Delete confirmation dialog
       if (uiState.showDeleteDialog) {
           AlertDialog(
               onDismissRequest = viewModel::dismissDeleteDialog,
               title = { Text("Delete Car") },
               text = { Text("Are you sure you want to delete '${uiState.profile?.name}'? This action cannot be undone.") },
               confirmButton = {
                   TextButton(
                       onClick = viewModel::deleteProfile,
                       colors = ButtonDefaults.textButtonColors(
                           contentColor = MaterialTheme.colorScheme.error
                       )
                   ) {
                       Text("Delete")
                   }
               },
               dismissButton = {
                   TextButton(onClick = viewModel::dismissDeleteDialog) {
                       Text("Cancel")
                   }
               }
           )
       }

       Scaffold(
           topBar = {
               TopAppBar(
                   title = { Text(uiState.profile?.name ?: "Loading...") },
                   navigationIcon = {
                       IconButton(onClick = onNavigateBack) {
                           Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                       }
                   },
                   actions = {
                       uiState.profile?.let { profile ->
                           IconButton(onClick = { onNavigateToEdit(profile.id) }) {
                               Icon(Icons.Default.Edit, contentDescription = "Edit")
                           }
                           IconButton(onClick = viewModel::showDeleteDialog) {
                               Icon(Icons.Default.Delete, contentDescription = "Delete")
                           }
                       }
                   }
               )
           }
       ) { paddingValues ->
           when {
               uiState.isLoading -> {
                   Box(
                       modifier = Modifier
                           .fillMaxSize()
                           .padding(paddingValues),
                       contentAlignment = Alignment.Center
                   ) {
                       CircularProgressIndicator()
                   }
               }
               uiState.profile == null -> {
                   Box(
                       modifier = Modifier
                           .fillMaxSize()
                           .padding(paddingValues),
                       contentAlignment = Alignment.Center
                   ) {
                       Text("Profile not found")
                   }
               }
               else -> {
                   val profile = uiState.profile!!

                   Column(
                       modifier = Modifier
                           .fillMaxSize()
                           .padding(paddingValues)
                           .verticalScroll(rememberScrollState())
                           .padding(16.dp),
                       verticalArrangement = Arrangement.spacedBy(16.dp)
                   ) {
                       // Quick Action Buttons
                       Row(
                           modifier = Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.spacedBy(8.dp)
                       ) {
                           OutlinedButton(
                               onClick = { onNavigateToTimer(profile.id) },
                               modifier = Modifier.weight(1f)
                           ) {
                               Icon(Icons.Default.Timer, contentDescription = null)
                               Spacer(Modifier.width(8.dp))
                               Text("Start Timing")
                           }
                           OutlinedButton(
                               onClick = { onNavigateToRpm(profile.id) },
                               modifier = Modifier.weight(1f)
                           ) {
                               Icon(Icons.Default.Speed, contentDescription = null)
                               Spacer(Modifier.width(8.dp))
                               Text("Measure RPM")
                           }
                       }

                       // Chassis Info
                       profile.chassis?.let { chassis ->
                           DetailSection("Chassis") {
                               DetailRow("Type", "${chassis.code} - ${chassis.name}")
                               DetailRow("Shaft Type", chassis.shaftType.capitalize())
                               DetailRow("Motor Position", chassis.motorPosition.capitalize())
                           }
                       }

                       // Motor Info
                       profile.motor?.let { motor ->
                           DetailSection("Motor") {
                               DetailRow("Type", motor.name)
                               DetailRow("Category", motor.category.capitalize())
                               DetailRow("RPM Range", "${motor.rpmMin.formatRpm()} - ${motor.rpmMax.formatRpm()}")
                               DetailRow("Break-in", if (motor.breakInStatus) "Complete" else "Not done")
                               if (motor.runCount > 0) {
                                   DetailRow("Run Count", motor.runCount.toString())
                               }
                           }
                       }

                       // Gear Ratio Info
                       profile.gearRatio?.let { gearRatio ->
                           DetailSection("Gear Ratio") {
                               GearRatioIndicator(
                                   gearRatio = gearRatio,
                                   modifier = Modifier.padding(vertical = 8.dp)
                               )
                           }
                       }

                       // Tires
                       if (profile.tires.frontType != null || profile.tires.rearType != null) {
                           DetailSection("Tires") {
                               profile.tires.frontType?.let {
                                   DetailRow("Front", buildString {
                                       append(it)
                                       profile.tires.frontMaterial?.let { m -> append(" ($m)") }
                                       profile.tires.frontDiameterMm?.let { d -> append(" - ${d}mm") }
                                   })
                               }
                               profile.tires.rearType?.let {
                                   DetailRow("Rear", buildString {
                                       append(it)
                                       profile.tires.rearMaterial?.let { m -> append(" ($m)") }
                                       profile.tires.rearDiameterMm?.let { d -> append(" - ${d}mm") }
                                   })
                               }
                           }
                       }

                       // Additional Info
                       if (profile.bodyType != null || profile.totalWeightG != null || profile.batteryType != null) {
                           DetailSection("Additional") {
                               profile.bodyType?.let { DetailRow("Body", it) }
                               profile.totalWeightG?.let { DetailRow("Weight", "${it}g") }
                               profile.batteryType?.let {
                                   DetailRow("Battery", buildString {
                                       append(it)
                                       profile.batteryBrand?.let { b -> append(" ($b)") }
                                   })
                               }
                           }
                       }

                       // Custom Mods
                       profile.customModNotes?.let { notes ->
                           DetailSection("Custom Modifications") {
                               Text(
                                   text = notes,
                                   style = MaterialTheme.typography.bodyMedium
                               )
                           }
                       }

                       // Tags
                       if (profile.tags.isNotEmpty()) {
                           DetailSection("Tags") {
                               Row(
                                   horizontalArrangement = Arrangement.spacedBy(8.dp),
                                   modifier = Modifier.padding(top = 4.dp)
                               ) {
                                   profile.tags.forEach { tag ->
                                       SuggestionChip(
                                           onClick = {},
                                           label = { Text(tag) }
                                       )
                                   }
                               }
                           }
                       }

                       Spacer(Modifier.height(32.dp))
                   }
               }
           }
       }
   }

   @Composable
   private fun DetailSection(
       title: String,
       content: @Composable ColumnScope.() -> Unit
   ) {
       Card(
           modifier = Modifier.fillMaxWidth()
       ) {
           Column(
               modifier = Modifier.padding(16.dp)
           ) {
               Text(
                   text = title,
                   style = MaterialTheme.typography.titleMedium,
                   color = MaterialTheme.colorScheme.primary
               )
               Spacer(Modifier.height(8.dp))
               content()
           }
       }
   }

   @Composable
   private fun DetailRow(label: String, value: String) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(vertical = 4.dp),
           horizontalArrangement = Arrangement.SpaceBetween
       ) {
           Text(
               text = label,
               style = MaterialTheme.typography.bodyMedium,
               color = MaterialTheme.colorScheme.onSurfaceVariant
           )
           Text(
               text = value,
               style = MaterialTheme.typography.bodyMedium
           )
       }
   }

   private fun Int.formatRpm(): String = "%,d".format(this)
   private fun String.capitalize(): String = replaceFirstChar { it.uppercase() }
   ```

---

## Task 1.6: Navigation Integration

### Description
Integrate all profile screens into the app navigation.

### Prerequisites
- Tasks 1.3, 1.4, 1.5 completed

### Acceptance Criteria
- [ ] Garage screen accessible from bottom navigation
- [ ] Navigation to profile detail from garage
- [ ] Navigation to create/edit profile screens
- [ ] Back navigation works correctly
- [ ] Deep linking support for profile detail
- [ ] Garage FAB/action opens create flow; save returns to Garage list with updated data

### Implementation Steps

1. **Update Navigation Destinations**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/navigation/Destinations.kt
   package com.mini4wd.lab.navigation

   sealed class Screen(val route: String) {
       // Bottom nav destinations
       data object Garage : Screen("garage")
       data object Timer : Screen("timer")
       data object Rpm : Screen("rpm")
       data object Stats : Screen("stats")

       // Profile feature destinations
       data object ProfileDetail : Screen("profile/{profileId}") {
           fun createRoute(profileId: Long) = "profile/$profileId"
       }

       data object ProfileCreate : Screen("profile/create")

       data object ProfileEdit : Screen("profile/{profileId}/edit") {
           fun createRoute(profileId: Long) = "profile/$profileId/edit"
       }
   }
   ```

2. **Update AppNavHost**
   ```kotlin
   // app/src/main/java/com/mini4wd/lab/navigation/AppNavHost.kt
   package com.mini4wd.lab.navigation

   import androidx.compose.runtime.Composable
   import androidx.compose.ui.Modifier
   import androidx.navigation.NavHostController
   import androidx.navigation.NavType
   import androidx.navigation.compose.NavHost
   import androidx.navigation.compose.composable
   import androidx.navigation.navArgument
   import com.mini4wd.lab.feature.profile.ui.detail.ProfileDetailScreen
   import com.mini4wd.lab.feature.profile.ui.edit.ProfileEditScreen
   import com.mini4wd.lab.feature.profile.ui.garage.GarageScreen
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
           // Bottom nav destinations
           composable(Screen.Garage.route) {
               GarageScreen(
                   onNavigateToProfile = { id ->
                       navController.navigate(Screen.ProfileDetail.createRoute(id))
                   },
                   onNavigateToCreateProfile = {
                       navController.navigate(Screen.ProfileCreate.route)
                   }
               )
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

           // Profile feature destinations
           composable(
               route = Screen.ProfileDetail.route,
               arguments = listOf(
                   navArgument("profileId") { type = NavType.LongType }
               )
           ) {
               ProfileDetailScreen(
                   onNavigateBack = { navController.popBackStack() },
                   onNavigateToEdit = { id ->
                       navController.navigate(Screen.ProfileEdit.createRoute(id))
                   },
                   onNavigateToTimer = { id ->
                       // TODO: Navigate to timer with profile selected
                       navController.navigate(Screen.Timer.route)
                   },
                   onNavigateToRpm = { id ->
                       // TODO: Navigate to RPM with profile selected
                       navController.navigate(Screen.Rpm.route)
                   }
               )
           }

           composable(Screen.ProfileCreate.route) {
               ProfileEditScreen(
                   onNavigateBack = { navController.popBackStack() }
               )
           }

           composable(
               route = Screen.ProfileEdit.route,
               arguments = listOf(
                   navArgument("profileId") { type = NavType.LongType }
               )
           ) {
               ProfileEditScreen(
                   onNavigateBack = { navController.popBackStack() }
               )
           }
       }
   }
   ```

3. **Test Navigation Flow**
   - Launch app → Garage screen displays
   - Tap FAB → Create profile screen
   - Fill form and save → Returns to Garage with new profile
   - Tap profile → Detail screen
   - Tap Edit → Edit screen with populated data
   - Save changes → Returns to Detail with updated info
   - Delete profile → Returns to Garage

---

## Milestone 1 Completion Checklist

- [ ] Task 1.1: CarProfile Entity and DAO
- [ ] Task 1.2: Domain Layer - CarProfile Repository
- [ ] Task 1.3: Garage Screen - Profile List UI
- [ ] Task 1.4: Profile Create/Edit Screen
- [ ] Task 1.5: Profile Detail Screen
- [ ] Task 1.6: Navigation Integration

**Definition of Done:**
- Users can create, view, edit, and delete car profiles
- Profiles store all configuration fields from ERD
- Motor and gear ratio selections filter based on chassis shaft type
- Search and tag filtering works on garage screen
- Visual gear ratio indicator displays correct colors
- All navigation flows work correctly
- Database migrations work without data loss
- Unit tests pass for repository and DAO operations
