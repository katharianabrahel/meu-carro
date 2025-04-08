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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    fun login(@Body login: LoginResquest): Call<LoginResponse>

    @POST("users")
    fun createUser(@Body user: UserRequest): Call<UserResponse>

    @POST("maintences/sync")
    fun syncMaintence(@Body sync: List<SyncMaintenceRequest>): Call<SyncMaintenceResponse>

    // Supondo que MaintenceRequest contenha um ID ou outro campo simples, adapte conforme necessário
    @GET("maintences/sync")
    fun getSyncMaintence(@Query("id") id: String): Call<List<MaintenceResponse>>
}
