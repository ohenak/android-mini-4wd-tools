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
