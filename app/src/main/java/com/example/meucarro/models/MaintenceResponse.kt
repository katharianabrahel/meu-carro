package com.example.meucarro.models

import java.util.Date

data class MaintenceResponse(
    val id: String? = null,
    val userId: String,
    val clientId: String,
    val name: String,
    val description: String? = null,
    val odometer: Int? = null,
    val performedAt: Date,
    val nextDueAt: Date,
    val deleted: Boolean? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
