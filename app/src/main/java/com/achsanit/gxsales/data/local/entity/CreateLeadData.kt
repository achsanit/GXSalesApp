package com.achsanit.gxsales.data.local.entity

import java.io.File

data class CreateLeadData(
    val branchOfficeId: Int,
    val probabilityId: Int,
    val typeId: Int,
    val channelId: Int,
    val mediaId: Int,
    val sourceId: Int,
    val statusId: Int,
    val fullName: String,
    val email: String,
    val prefixPhone: String,
    val phone: String,
    val address: String,
    val latitude: String,
    val longitude: String,
    val companyName: String,
    val gender: String,
    val idNumber: String,
    val idNumberPhoto: File?,
    val notes: String
)