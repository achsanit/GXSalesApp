package com.achsanit.gxsales.ui.features.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.data.local.entity.ProfileEntity
import com.achsanit.gxsales.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogoutViewModel(private val mainRepo: MainRepository): ViewModel() {

    private val _profileState = MutableStateFlow<Resource<ProfileEntity>>(Resource.Loading())
    val profileState = _profileState.asStateFlow()

    private val _logoutState = MutableSharedFlow<Resource<Boolean>>()
    val logoutState = _logoutState.asSharedFlow()

    init {
        getProfile()
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.emit(Resource.Loading())
            _logoutState.emit(mainRepo.logout())
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            _profileState.emit(mainRepo.getProfile())
        }
    }
}