package com.achsanit.gxsales.data.network.service

import com.achsanit.gxsales.data.network.response.LeadsResponse
import com.achsanit.gxsales.data.network.response.GetProfileResponse
import com.achsanit.gxsales.data.network.response.LeadsDashboardResponse
import com.achsanit.gxsales.data.network.response.LoginResponse
import com.achsanit.gxsales.data.network.response.MetaResponse
import com.achsanit.gxsales.data.network.response.SettingsResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("leads")
    suspend fun getLeads(): LeadsResponse

    @GET("settings/branch-offices")
    suspend fun getBranchOffices(): SettingsResponse

    @GET("settings/types")
    suspend fun getLeadTypes(): SettingsResponse

    @GET("settings/channels")
    suspend fun getLeadChannels(): SettingsResponse

    @GET("settings/medias")
    suspend fun getLeadMedia(): SettingsResponse

    @GET("settings/sources")
    suspend fun getLeadSources(): SettingsResponse

    @GET("settings/statuses")
    suspend fun getLeadStatuses(): SettingsResponse

    @GET("settings/probabilities")
    suspend fun getLeadProbabilities(): SettingsResponse

    @POST("leads")
    suspend fun createLead(
        @Body body: RequestBody
    ): MetaResponse

    @DELETE("leads/{leadId}")
    suspend fun deleteLead(
        @Path("leadId") id: Int
    ): MetaResponse
}