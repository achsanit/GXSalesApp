package com.achsanit.gxsales.utils.mapper

import com.achsanit.gxsales.data.local.entity.LeadDashboardEntity
import com.achsanit.gxsales.data.local.entity.LeadItemEntity
import com.achsanit.gxsales.data.network.response.LeadsDashboardResponse
import com.achsanit.gxsales.data.network.response.LeadsResponse
import com.achsanit.gxsales.utils.DateHelper

fun LeadsDashboardResponse.map(): List<LeadDashboardEntity> {
    return this.data?.statuses?.map {
        LeadDashboardEntity(
            name = it?.name ?: "",
            total = it?.total ?: 0
        )
    } ?: emptyList()
}

fun LeadsResponse.map(): List<LeadItemEntity> {
    return this.data?.map { lead ->
        LeadItemEntity(
            id = lead?.id ?: 0,
            numberLead = lead?.number ?: "",
            fullName = lead?.fullName ?: "",
            address = lead?.address ?: "",
            branchOffice = lead?.branchOffice?.name ?: "",
            status = lead?.status?.name?.let { status ->
                val statusSplit = status.split(" ")
                if (statusSplit.size > 2) statusSplit.first() else status
            } ?: "null",
            probability = lead?.probability?.name ?: "null",
            createdAt = lead?.createdAt?.let { DateHelper.convertDateFormat(it) } ?: ""
        )
    } ?: emptyList()
}