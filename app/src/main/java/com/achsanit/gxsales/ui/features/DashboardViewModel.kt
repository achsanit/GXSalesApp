package com.achsanit.gxsales.ui.features

import androidx.lifecycle.ViewModel
import com.achsanit.gxsales.data.local.entity.LeadDashboardEntity

class DashboardViewModel : ViewModel() {

    val listLeadStatus = listOf(
        LeadDashboardEntity(name = "Elba Cleveland", total = 8749),
        LeadDashboardEntity(name = "Rafael Decker", total = 6235),
        LeadDashboardEntity(name = "Elba Cleveland", total = 8749),
        LeadDashboardEntity(name = "Rafael Decker", total = 6235),
        LeadDashboardEntity(name = "Elba Cleveland", total = 8749),
        LeadDashboardEntity(name = "Rafael Decker", total = 6235),
        LeadDashboardEntity(name = "Elba Cleveland", total = 8749),
        LeadDashboardEntity(name = "Rafael Decker", total = 6235),
    )
}