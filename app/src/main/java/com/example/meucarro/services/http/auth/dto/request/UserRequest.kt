package com.example.meucarro.services.http.auth.dto.request

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
)
