package com.example.meucarro.services.http.maintenance.dto.request

import java.time.LocalDateTime

data class CreateMaintenanceRequest(
    val clientId: String,
    val name: String,
    val description: String,
    val odometer: Int,
    val performedAt: String,
    val nextDueAt: String,
)
