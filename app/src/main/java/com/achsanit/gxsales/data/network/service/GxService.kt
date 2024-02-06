package com.achsanit.gxsales.data.network.service

import com.achsanit.gxsales.data.network.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GxService {

    @POST("login")
    suspend fun login(
        @Body body: HashMap<String, String>
    ): LoginResponse

}