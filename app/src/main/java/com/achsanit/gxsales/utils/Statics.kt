package com.achsanit.gxsales.utils

object Statics {
    /**
     * this variable for compare with response status from API
     * or anything that want to check equals with success or error
     * e.g : response.api.status.equals(Statics.SUCCESS) -> to check if response is success
     */
    val SUCCESS = "success"
    val ERROR = "error"


}

// this sealed interface for get setting lead data
sealed interface LeadSettings {
    data object Type: LeadSettings
    data object Channel: LeadSettings
    data object Media: LeadSettings
    data object Source: LeadSettings
    data object Status: LeadSettings
    data object Probability: LeadSettings
    data object BranchOffice: LeadSettings
}