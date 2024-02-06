package com.achsanit.gxsales.data

import com.achsanit.gxsales.data.local.DataStorePreference
import com.achsanit.gxsales.data.network.response.LoginResponse
import com.achsanit.gxsales.data.network.service.GxService
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.resourceMapper
import kotlinx.coroutines.flow.Flow

class MainRepository(
    private val service: GxService,
    private val dataPref: DataStorePreference
) {

    // function login with param email and password
    suspend fun login(
        email: String,
        password: String
    ): Resource<LoginResponse> {
        val body = HashMap<String, String>()
        body["email"] = email
        body["password"] = password

        return resourceMapper { service.login(body) }
    }

    // save login data to local storage (data store pref)
    suspend fun saveLoginData(token: String) {
        dataPref.setLoginData(token)
    }

    // get token by local storage
    fun getToken(): Flow<String> {
        return dataPref.getToken()
    }

    // get is login or not
    suspend fun isLogin(): Boolean {
        return dataPref.isLogin()
    }

}