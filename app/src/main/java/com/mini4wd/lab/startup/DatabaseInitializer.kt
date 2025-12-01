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
