package com.mini4wd.lab.feature.profile.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
