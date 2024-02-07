package com.achsanit.gxsales.data.network.service

import com.achsanit.gxsales.data.network.response.GetProfileResponse
import com.achsanit.gxsales.data.network.response.LeadsDashboardResponse
import com.achsanit.gxsales.data.network.response.LoginResponse
import com.achsanit.gxsales.data.network.response.MetaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GxService {

    @POST("login")
    suspend fun login(
        @Body body: HashMap<String, String>
    ): LoginResponse

    @GET("leads/dashboard")
    suspend fun getLeadsDashboard(): LeadsDashboardResponse

    @GET("profile")
    suspend fun getProfile(): GetProfileResponse

    @POST("logout")
    suspend fun logout(): MetaResponse
}