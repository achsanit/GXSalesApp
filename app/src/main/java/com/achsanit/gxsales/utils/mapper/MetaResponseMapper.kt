package com.achsanit.gxsales.utils.mapper

import com.achsanit.gxsales.data.network.response.MetaResponse
import com.achsanit.gxsales.utils.Statics

fun MetaResponse.isSuccess(): Boolean {
    return this.status.equals(Statics.SUCCESS)
}