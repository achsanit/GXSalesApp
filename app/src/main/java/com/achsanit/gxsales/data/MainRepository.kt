package com.achsanit.gxsales.data

import com.achsanit.gxsales.data.local.DataStorePreference
import com.achsanit.gxsales.data.local.SharedPreferencesManager
import com.achsanit.gxsales.data.local.entity.LeadDashboardEntity
import com.achsanit.gxsales.data.local.entity.ProfileEntity
import com.achsanit.gxsales.data.network.response.LoginResponse
import com.achsanit.gxsales.data.network.service.GxService
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.mapper.isSuccess
import com.achsanit.gxsales.utils.mapper.map
import com.achsanit.gxsales.utils.resourceMapper

class MainRepository(
    private val service: GxService,
    private val dataPref: DataStorePreference,
    private val sharedPref: SharedPreferencesManager
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
        sharedPref.saveData(SharedPreferencesManager.TOKEN_KEY, token)
        dataPref.setLoginData(token)
    }

    // get is login or not
    suspend fun isLogin(): Boolean {
        return dataPref.isLogin()
    }

    // function get service leads dashboard
    suspend fun getLeadsDashboard(): Resource<List<LeadDashboardEntity>> {
        return resourceMapper {
            service.getLeadsDashboard().map()
        }
    }

    // function get user profile
    suspend fun getProfile(): Resource<ProfileEntity> {
        return resourceMapper {
            service.getProfile().map()
        }
    }

    // function logout
    suspend fun logout(): Resource<Boolean> {
        // request to logout
        val request = resourceMapper { service.logout().isSuccess() }

        // delete token and status login from local storage
        dataPref.setLogoutData()
        sharedPref.saveData(SharedPreferencesManager.TOKEN_KEY, "")

        return request
    }
}