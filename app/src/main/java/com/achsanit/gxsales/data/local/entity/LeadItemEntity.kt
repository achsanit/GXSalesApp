package com.achsanit.gxsales.data.local.entity

data class LeadItemEntity(
    val id: Int,
    val numberLead: String,
    val fullName: String,
    val address: String,
    val branchOffice: String,
    val status: String,
    val probability: String,
    val createdAt: String
)
