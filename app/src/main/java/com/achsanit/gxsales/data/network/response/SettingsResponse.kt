package com.achsanit.gxsales.data.network.response

import com.google.gson.annotations.SerializedName

data class SettingsResponse(

	@field:SerializedName("data")
	val data: List<DataSettingItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataSettingItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
