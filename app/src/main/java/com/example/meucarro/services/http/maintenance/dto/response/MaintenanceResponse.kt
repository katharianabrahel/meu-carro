package com.example.meucarro.services.http.maintenance.dto.response

import java.time.LocalDateTime

data class MaintenanceResponse(
    val id: String,
    val name: String,
    val clientId: String,
    val description: String,
    val odometer: Int,
    val performedAt: String,
    val nextDueAt: String,
    val createdAt: String,
    val updatedAt: String,
)
