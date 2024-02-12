package com.achsanit.gxsales.ui.features.lead.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.data.local.entity.LeadItemEntity
import com.achsanit.gxsales.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeadsViewModel(private val mainRepo: MainRepository) : ViewModel() {

    private val _leadsState = MutableStateFlow<Resource<List<LeadItemEntity>>>(Resource.Loading())
    val leadsState = _leadsState.asStateFlow()

    fun getLeads() {
        viewModelScope.launch {
            _leadsState.emit(mainRepo.getLeads())
        }
    }
}