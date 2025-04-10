package com.example.meucarro.services.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.UUID
import java.util.concurrent.TimeUnit

fun scheduleReminder(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<MaintenanceReminderWorker>(
        15, TimeUnit.MINUTES // mínimo para PeriodicWorkRequest
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "maintenance_reminder",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}

fun testReminderNow(context: Context) {
    val testRequest = androidx.work.OneTimeWorkRequestBuilder<MaintenanceReminderWorker>()
        .build()

    WorkManager.getInstance(context).enqueue(testRequest)
}

fun cancelReminder(context: Context) {
    WorkManager.getInstance(context).cancelUniqueWork("maintenance_reminder")
}
