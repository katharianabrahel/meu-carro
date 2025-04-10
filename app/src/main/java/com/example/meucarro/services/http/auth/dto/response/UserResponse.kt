package com.example.meucarro.services.http.auth.dto.response

data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val photo: String,
    val role: String,
    val createdAt: String
)
