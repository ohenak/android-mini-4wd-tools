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
