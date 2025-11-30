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
