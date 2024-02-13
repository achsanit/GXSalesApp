package com.achsanit.gxsales.utils.mapper

import com.achsanit.gxsales.data.network.response.SettingsResponse

fun SettingsResponse.toHashMap(): HashMap<String, Int> {
    val hashMap = HashMap<String, Int>()
    this.data?.map {
        it?.let {
            if (!it.name.isNullOrEmpty() && it.id != null) {
                hashMap[it.name] = it.id
            }
        }
    }
    return hashMap
}