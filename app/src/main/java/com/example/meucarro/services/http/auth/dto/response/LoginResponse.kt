package com.example.meucarro.services.http.auth.dto.response


data class LoginResponse(
    val token: String,
    val user: UserResponse
)
