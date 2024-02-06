package com.achsanit.gxsales.data

import com.achsanit.gxsales.data.response.LoginResponse
import com.achsanit.gxsales.data.service.GxService
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.resourceMapper

class MainRepository(private val service: GxService) {

    suspend fun login(
        email: String,
        password: String
    ): Resource<LoginResponse> {
        val body = HashMap<String, String>()
        body["email"] = email
        body["password"] = password

        return resourceMapper { service.login(body) }
    }

}