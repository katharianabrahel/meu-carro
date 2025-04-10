package com.example.meucarro.services.http.maintenance

import com.example.meucarro.services.http.maintenance.dto.request.CreateMaintenanceRequest
import com.example.meucarro.services.http.maintenance.dto.response.MaintenanceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MaintenanceService {
    @POST("maintences")
    suspend fun createMaintenance(
        @Body maintenanceRequest: CreateMaintenanceRequest
    ): MaintenanceResponse

    @GET("maintences")
    suspend fun getMaintenances(): List<MaintenanceResponse>

    @GET("maintences/{id}")
    suspend fun getMaintenanceById(
        @Body id: String
    ): MaintenanceResponse

    @PUT("maintences/{id}")
    suspend fun updateMaintenance(
        @Path("id") id: String,
        @Body maintenanceRequest: CreateMaintenanceRequest
    ): MaintenanceResponse
}