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
