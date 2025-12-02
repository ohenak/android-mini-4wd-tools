package com.mini4wd.lab.feature.profile.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
