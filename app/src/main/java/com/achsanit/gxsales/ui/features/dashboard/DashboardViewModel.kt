package com.achsanit.gxsales.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.data.local.entity.LeadDashboardEntity
import com.achsanit.gxsales.data.local.entity.ProfileEntity
import com.achsanit.gxsales.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(private val mainRepo: MainRepository) : ViewModel() {

    private val _leadsDashboardState =
        MutableStateFlow<Resource<List<LeadDashboardEntity>>>(Resource.Loading())
    val leadsDashboardState = _leadsDashboardState.asStateFlow()

    private val _profileState = MutableStateFlow<Resource<ProfileEntity>>(Resource.Loading())
    val profileState = _profileState.asStateFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            _profileState.emit(mainRepo.getProfile())
        }
    }

    fun getLeads() {
        viewModelScope.launch {
            _leadsDashboardState.emit(mainRepo.getLeadsDashboard())
        }
    }
}