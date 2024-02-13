package com.achsanit.gxsales.utils.mapper

import com.achsanit.gxsales.data.local.entity.ProfileEntity
import com.achsanit.gxsales.data.network.response.GetProfileResponse

fun GetProfileResponse.map(): ProfileEntity {
    return this.data.let {
        ProfileEntity(
            id = it?.id ?: 0,
            name = it?.name ?: "",
            email = it?.email ?: ""
        )
    }
}