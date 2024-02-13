package com.achsanit.gxsales.data.local.entity

data class DetailLeadEntity(
    val id: Int,
    val fullName: String,
    val branchOfficeName: String,
    val branchId: Int,
    val email: String,
    val phone: String,
    val address: String,
    val latitude: String,
    val longitude: String,
    val gender: String,
    val idNumber: String,
    val idNumberPhoto: String,
    val notes: String,
    val statusId: Int,
    val statusName: String,
    val probabilityId: Int,
    val probabilityName: String,
    val channelId: Int,
    val channelName: String,
    val typeId: Int,
    val typeName: String,
    val mediaId: Int,
    val mediaName: String,
    val sourceId: Int,
    val sourceName: String
)
