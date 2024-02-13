package com.achsanit.gxsales.data.network.response

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(

	@field:SerializedName("data")
	val data: ProfileData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ProfileData(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
