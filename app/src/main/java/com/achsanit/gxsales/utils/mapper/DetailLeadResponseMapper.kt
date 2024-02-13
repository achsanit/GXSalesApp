package com.achsanit.gxsales.utils.mapper

import com.achsanit.gxsales.data.local.entity.DetailLeadEntity
import com.achsanit.gxsales.data.network.response.DetailLeadResponse

fun DetailLeadResponse.map(): DetailLeadEntity {
    return this.let {
        DetailLeadEntity(
            id = it.id ?: 0,
            fullName = it.fullName ?: "",
            branchOfficeName = it.branchOffice?.name ?: "",
            branchId = it.branchOffice?.id ?: 0,
            email = it.email ?: "",
            phone = it.phone ?: "",
            address = it.address ?: "",
            latitude = it.latitude ?: "",
            longitude = it.longitude ?: "",
            gender = it.gender ?: "",
            idNumber = it.iDNumber ?: "",
            idNumberPhoto = it.iDNumberPhoto ?: "",
            notes = it.generalNotes ?: "",
            statusId = it.status?.id ?: 0,
            statusName = it.status?.name ?: "",
            probabilityId = it.probability?.id ?: 0,
            probabilityName = it.probability?.name ?: "",
            channelId = it.channel?.id ?: 0,
            channelName =it.channel?.name ?: "",
            typeId = it.type?.id ?: 0,
            typeName = it.type?.name ?: "",
            mediaId = it.media?.id ?: 0,
            mediaName = it.media?.name ?: "",
            sourceId = it.source?.id ?: 0,
            sourceName = it.source?.name ?: ""
        )
    }
}