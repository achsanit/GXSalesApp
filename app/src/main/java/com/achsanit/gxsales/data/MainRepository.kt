package com.achsanit.gxsales.data

import com.achsanit.gxsales.data.local.DataStorePreference
import com.achsanit.gxsales.data.local.SharedPreferencesManager
import com.achsanit.gxsales.data.local.entity.CreateLeadData
import com.achsanit.gxsales.data.local.entity.LeadDashboardEntity
import com.achsanit.gxsales.data.local.entity.LeadItemEntity
import com.achsanit.gxsales.data.local.entity.ProfileEntity
import com.achsanit.gxsales.data.network.response.LoginResponse
import com.achsanit.gxsales.data.network.service.GxService
import com.achsanit.gxsales.utils.LeadSettings
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.Statics
import com.achsanit.gxsales.utils.mapper.isSuccess
import com.achsanit.gxsales.utils.mapper.map
import com.achsanit.gxsales.utils.mapper.toHashMap
import com.achsanit.gxsales.utils.resourceMapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

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

    // function get list of lead
    suspend fun getLeads(): Resource<List<LeadItemEntity>> {
        return resourceMapper {
            service.getLeads().map()
        }
    }

    // function to get setting for create and update lead
    suspend fun getSettingsData(
        settingType: LeadSettings // sealed class for check the setting that want to get
    ): Resource<HashMap<String,Int>> {
        return resourceMapper {
            when(settingType) { // checking setting type and get the data
                is LeadSettings.BranchOffice -> service.getBranchOffices().toHashMap()
                is LeadSettings.Type -> service.getLeadTypes().toHashMap()
                is LeadSettings.Channel -> service.getLeadChannels().toHashMap()
                is LeadSettings.Media -> service.getLeadMedia().toHashMap()
                is LeadSettings.Source -> service.getLeadSources().toHashMap()
                is LeadSettings.Status -> service.getLeadStatuses().toHashMap()
                is LeadSettings.Probability -> service.getLeadProbabilities().toHashMap()
            }
        }
    }

    // function to create and update lead
    suspend fun createLead(data: CreateLeadData): Resource<Boolean> {
        // request body for post data
        val phone = if (data.prefixPhone.isNotEmpty() && data.phone.first().toString() == "0") {
            data.phone.drop(1)
        } else {
            data.phone
        }
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("branchOfficeId", data.branchOfficeId.toString())
            .addFormDataPart("probabilityId", data.probabilityId.toString())
            .addFormDataPart("typeId", data.typeId.toString())
            .addFormDataPart("channelId", data.channelId.toString())
            .addFormDataPart("mediaId", data.mediaId.toString())
            .addFormDataPart("sourceId", data.sourceId.toString())
            .addFormDataPart("statusId", data.statusId.toString())
            .addFormDataPart("fullName", data.fullName)
            .addFormDataPart("email", data.email)
            .addFormDataPart("phone", "${data.prefixPhone}$phone")
            .addFormDataPart("address", data.address)
            .addFormDataPart("latitude", data.latitude)
            .addFormDataPart("longitude", data.longitude)
            .addFormDataPart("companyName", data.companyName)
            .addFormDataPart("generalNotes", data.notes)
            .addFormDataPart("gender", data.gender.lowercase())
            .addFormDataPart("IDNumber", data.idNumber)

        data.idNumberPhoto?.let {
            // multipart body image for send file
            val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                it.name.toString(),
                it.asRequestBody("image/png".toMediaTypeOrNull())
            )
            requestBody.addPart(filePart)
        }

        return resourceMapper {
            service.createLead(requestBody.build()).status.equals(Statics.SUCCESS)
        }
    }
}