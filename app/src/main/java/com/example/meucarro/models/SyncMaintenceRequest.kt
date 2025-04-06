package com.example.meucarro.models

import java.util.Date

data class SyncMaintenceRequest(
    val id: Int,
    val name: String,
    val clientId: String,
    val description: String? = null,
    val odometer: Int? = null,
    val performedAt: Date,
    val nextDueAt: Date,
    val updatedAt: Date,
    val deleted: Boolean? = null
)