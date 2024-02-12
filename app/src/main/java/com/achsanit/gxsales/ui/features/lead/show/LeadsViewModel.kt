package com.achsanit.gxsales.ui.features.lead.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.data.local.entity.LeadItemEntity
import com.achsanit.gxsales.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeadsViewModel(private val mainRepo: MainRepository) : ViewModel() {

    private val _leadsState = MutableStateFlow<Resource<List<LeadItemEntity>>>(Resource.Loading())
    val leadsState = _leadsState.asStateFlow()

    private val _deleteState = MutableSharedFlow<Resource<Boolean>>()
    val deleteState = _deleteState.asSharedFlow()

    fun getLeads() {
        viewModelScope.launch {
            _leadsState.emit(mainRepo.getLeads())
        }
    }

    fun deleteLead(id: Int) {
        viewModelScope.launch {
            _deleteState.emit(Resource.Loading())
            _deleteState.emit(mainRepo.deleteLead(id))
        }
    }
}