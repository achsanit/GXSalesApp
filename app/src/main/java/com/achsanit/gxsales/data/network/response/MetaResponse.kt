package com.achsanit.gxsales.data.network.response

import com.google.gson.annotations.SerializedName

data class MetaResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
