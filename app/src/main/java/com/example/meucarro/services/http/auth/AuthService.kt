package com.example.meucarro.services.http.auth

import com.example.meucarro.services.http.auth.dto.response.LoginResponse
import com.example.meucarro.services.http.auth.dto.request.LoginResquest
import com.example.meucarro.services.http.auth.dto.request.UserRequest
import com.example.meucarro.services.http.auth.dto.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body login: LoginResquest): LoginResponse

    @POST("users")
    suspend fun createUser(@Body user: UserRequest): UserResponse
}