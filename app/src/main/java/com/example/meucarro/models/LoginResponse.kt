package com.example.meucarro.models


data class LoginResponse(
    val token: String,
    val user: UserResponse
)
