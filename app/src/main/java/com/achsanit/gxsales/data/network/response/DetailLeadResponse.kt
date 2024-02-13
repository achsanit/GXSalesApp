package com.achsanit.gxsales.data.network.response

import com.google.gson.annotations.SerializedName

data class DetailLeadResponse(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("probability")
	val probability: Probability? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("companyName")
	val companyName: Any? = null,

	@field:SerializedName("channel")
	val channel: Channel? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("media")
	val media: Media? = null,

	@field:SerializedName("source")
	val source: Source? = null,

	@field:SerializedName("type")
	val type: Type? = null,

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("branchOffice")
	val branchOffice: BranchOffice? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("IDNumberPhoto")
	val iDNumberPhoto: String? = null,

	@field:SerializedName("generalNotes")
	val generalNotes: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("IDNumber")
	val iDNumber: String? = null,

	@field:SerializedName("status")
	val status: Status? = null
)