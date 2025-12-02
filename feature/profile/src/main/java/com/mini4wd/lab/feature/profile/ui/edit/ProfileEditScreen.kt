package com.mini4wd.lab.feature.profile.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mini4wd.lab.feature.profile.ui.components.ChassisDropdown
import com.mini4wd.lab.feature.profile.ui.components.GearRatioDropdown
import com.mini4wd.lab.feature.profile.ui.components.MotorDropdown

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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                contentAlignment = Alignment.Center
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
                        verticalAlignment = Alignment.CenterVertically
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
