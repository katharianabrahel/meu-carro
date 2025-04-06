package com.example.meucarro.services.http

import com.example.meucarro.models.LoginResponse
import com.example.meucarro.models.LoginResquest
import com.example.meucarro.models.MaintenceRequest
import com.example.meucarro.models.MaintenceResponse
import com.example.meucarro.models.SyncMaintenceRequest
import com.example.meucarro.models.SyncMaintenceResponse
import com.example.meucarro.models.UserRequest
import com.example.meucarro.models.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(login: LoginResquest): Call<LoginResponse>

    @POST("users")
    fun createUser(user: UserRequest): Call<UserResponse>

    @POST("maintences/sync")
    fun syncMaintence(sync: List<SyncMaintenceRequest>): Call<SyncMaintenceResponse>

    @GET("maintences/sync")
    fun getSyncMaintence(sync: MaintenceRequest): Call<List<MaintenceResponse>>
}