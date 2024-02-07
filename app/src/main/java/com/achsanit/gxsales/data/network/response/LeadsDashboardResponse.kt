package com.achsanit.gxsales.data.network.response

import com.google.gson.annotations.SerializedName

data class LeadsDashboardResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class StatusesItem(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class Data(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("statuses")
	val statuses: List<StatusesItem?>? = null
)
