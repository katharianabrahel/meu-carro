package com.example.meucarro.models

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
)
