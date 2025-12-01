package com.mini4wd.lab.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mini4wd.lab.core.database.dao.CarProfileDao
import com.mini4wd.lab.core.database.dao.ChassisRefDao
import com.mini4wd.lab.core.database.dao.GearRatioRefDao
import com.mini4wd.lab.core.database.dao.MotorRefDao
import com.mini4wd.lab.core.database.entity.CarProfileEntity
import com.mini4wd.lab.core.database.entity.ChassisRefEntity
import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
import com.mini4wd.lab.core.database.entity.MotorRefEntity

@Database(
    entities = [
        ChassisRefEntity::class,
        MotorRefEntity::class,
        GearRatioRefEntity::class,
        CarProfileEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class Mini4WDDatabase : RoomDatabase() {
    abstract fun chassisRefDao(): ChassisRefDao
    abstract fun motorRefDao(): MotorRefDao
    abstract fun gearRatioRefDao(): GearRatioRefDao
    abstract fun carProfileDao(): CarProfileDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `car_profile` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `chassis_ref_id` INTEGER,
                        `motor_ref_id` INTEGER,
                        `gear_ratio_ref_id` INTEGER,
                        `motor_break_in_status` INTEGER NOT NULL DEFAULT 0,
                        `motor_break_in_date` INTEGER,
                        `motor_run_count` INTEGER NOT NULL DEFAULT 0,
                        `custom_mod_notes` TEXT,
                        `tire_front_type` TEXT,
                        `tire_front_material` TEXT,
                        `tire_front_diameter_mm` REAL,
                        `tire_rear_type` TEXT,
                        `tire_rear_material` TEXT,
                        `tire_rear_diameter_mm` REAL,
                        `wheel_type` TEXT,
                        `wheel_material` TEXT,
                        `roller_config_json` TEXT,
                        `damper_config_json` TEXT,
                        `brake_config_json` TEXT,
                        `body_type` TEXT,
                        `body_aero_notes` TEXT,
                        `total_weight_g` REAL,
                        `battery_type` TEXT,
                        `battery_brand` TEXT,
                        `tags` TEXT,
                        `thumbnail_path` TEXT,
                        `created_at` INTEGER NOT NULL,
                        `updated_at` INTEGER NOT NULL,
                        FOREIGN KEY(`chassis_ref_id`) REFERENCES `chassis_ref`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL,
                        FOREIGN KEY(`motor_ref_id`) REFERENCES `motor_ref`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL,
                        FOREIGN KEY(`gear_ratio_ref_id`) REFERENCES `gear_ratio_ref`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_car_profile_chassis_ref_id` ON `car_profile` (`chassis_ref_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_car_profile_motor_ref_id` ON `car_profile` (`motor_ref_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_car_profile_gear_ratio_ref_id` ON `car_profile` (`gear_ratio_ref_id`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_car_profile_name` ON `car_profile` (`name`)")
            }
        }
    }
}
