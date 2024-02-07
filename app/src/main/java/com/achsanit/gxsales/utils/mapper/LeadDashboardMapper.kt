package com.achsanit.gxsales.utils.mapper

import com.achsanit.gxsales.data.local.entity.LeadDashboardEntity
import com.achsanit.gxsales.data.network.response.LeadsDashboardResponse

fun LeadsDashboardResponse.map(): List<LeadDashboardEntity> {
    return this.data?.statuses?.map {
        LeadDashboardEntity(
            name = it?.name ?: "",
            total = it?.total ?: 0
        )
    } ?: emptyList()
}