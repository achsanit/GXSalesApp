package com.achsanit.gxsales.ui.features.lead.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.data.local.entity.CreateLeadData
import com.achsanit.gxsales.utils.LeadSettings
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.isValidEmail
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class AddLeadViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _mainData: MutableStateFlow<CreateLeadData> = MutableStateFlow(initialized())
    val mainData = _mainData.asStateFlow()

    private val _getBranchState: MutableStateFlow<Resource<HashMap<String, Int>>> =
        MutableStateFlow(Resource.Loading())
    val getBranchState = _getBranchState.asStateFlow()

    private val _leadTypeState: MutableStateFlow<Resource<HashMap<String, Int>>> =
        MutableStateFlow(Resource.Loading())
    val leadTypeState = _leadTypeState.asStateFlow()

    private val _leadChannelState: MutableStateFlow<Resource<HashMap<String, Int>>> =
        MutableStateFlow(Resource.Loading())
    val leadChannelState = _leadChannelState.asStateFlow()

    private val _leadMediaState: MutableStateFlow<Resource<HashMap<String, Int>>> =
        MutableStateFlow(Resource.Loading())
    val leadMediaState = _leadMediaState.asStateFlow()

    private val _leadSourceState: MutableStateFlow<Resource<HashMap<String, Int>>> =
        MutableStateFlow(Resource.Loading())
    val leadSourceState = _leadSourceState.asStateFlow()

    private val _leadStatusState: MutableStateFlow<Resource<HashMap<String, Int>>> =
        MutableStateFlow(Resource.Loading())
    val leadStatusState = _leadStatusState.asStateFlow()

    private val _leadProbabilityState: MutableStateFlow<Resource<HashMap<String, Int>>> =
        MutableStateFlow(Resource.Loading())
    val leadProbabilityState = _leadProbabilityState.asStateFlow()

    val selectedDropdownItem = MutableStateFlow(selectedDropdownItem())

    private val _createLeadState: MutableSharedFlow<Resource<Boolean>> = MutableSharedFlow()
    val createLeadState = _createLeadState.asSharedFlow()

    private val _buttonNextState = MutableStateFlow(false)
    val buttonNextState = _buttonNextState.asStateFlow()
    private val _buttonSubmitState = MutableStateFlow(false)
    val buttonSubmitState: StateFlow<Boolean> = _buttonSubmitState

    init {
        getBranchOffices()
    }

    fun getSettingsData() {
        getTypes()
        getChannel()
        getMedia()
        getSource()
        getStatus()
        getProbability()
    }
    private fun getBranchOffices() {
        viewModelScope.launch {
            _getBranchState.emit(mainRepository.getSettingsData(LeadSettings.BranchOffice))
        }
    }
    private fun getTypes() {
        viewModelScope.launch {
            _leadTypeState.emit(mainRepository.getSettingsData(LeadSettings.Type))
        }
    }
    private fun getChannel() {
        viewModelScope.launch {
            _leadChannelState.emit(mainRepository.getSettingsData(LeadSettings.Channel))
        }
    }
    private fun getMedia() {
        viewModelScope.launch {
            _leadMediaState.emit(mainRepository.getSettingsData(LeadSettings.Media))
        }
    }
    private fun getSource() {
        viewModelScope.launch {
            _leadSourceState.emit(mainRepository.getSettingsData(LeadSettings.Source))
        }
    }
    private fun getStatus() {
        viewModelScope.launch {
            _leadStatusState.emit(mainRepository.getSettingsData(LeadSettings.Status))
        }
    }
    private fun getProbability() {
        viewModelScope.launch {
            _leadProbabilityState.emit(mainRepository.getSettingsData(LeadSettings.Probability))
        }
    }

    fun setToDefaultState() {
        viewModelScope.launch {
            _mainData.emit(initialized())
            selectedDropdownItem.emit(selectedDropdownItem())
        }
    }

    fun dispatch(event: UIEvent) {
        viewModelScope.launch {
            when(event) {
                is UIEvent.OnBranchOfficeChanged -> {
                    selectedDropdownItem.update { it.copy(branchOffice = event.text) }
                    val id: Int = when(val branch = getBranchState.value) {
                        is Resource.Loading -> 0
                        is Resource.Success -> {
                            branch.data?.let {
                                it[event.text]
                            } ?: 0
                        }
                        else -> -1
                    }
                    _mainData.update { it.copy(branchOfficeId = id) }
                }
                is UIEvent.OnFullNameChanged -> {
                    _mainData.update { it.copy(fullName = event.text) }
                }
                is UIEvent.OnEmailChanged -> {
                    _mainData.update { it.copy(email = event.text) }
                }
                is UIEvent.OnPrefixPhoneChanged -> {
                    selectedDropdownItem.update { it.copy(prefixPhone = event.text) }
                    _mainData.update {
                        it.copy(prefixPhone = event.text)
                    }
                }
                is UIEvent.OnPhoneChanged -> {
                    _mainData.update { it.copy(phone = event.text) }
                }
                is UIEvent.OnLocationChanged -> {
                    _mainData.update {
                        it.copy(
                            latitude = event.latitude,
                            longitude = event.longitude,
                            address = event.address
                        )
                    }
                }
                is UIEvent.OnAddressChanged -> {
                    _mainData.update { it.copy(address = event.text) }
                }
                is UIEvent.OnGenderChanged -> {
                    _mainData.update { it.copy(gender = event.text) }
                }
                is UIEvent.OnIdentityNumberChanged -> {
                    _mainData.update { it.copy(idNumber = event.text) }
                }
                is UIEvent.OnImageIDNumberChanged -> {
                    run {
                        _mainData.update { it.copy(idNumberPhoto = event.file) }
                    }
                }
                is UIEvent.OnLeadTypeChanged -> {
                    selectedDropdownItem.update { it.copy(leadType = event.text) }
                    val id = settingsHashMapGetValue(event.text, leadTypeState.value)
                    _mainData.update { it.copy(typeId = id) }
                }
                is UIEvent.OnLeadChannelChanged -> {
                    selectedDropdownItem.update { it.copy(leadChannel = event.text) }
                    val id = settingsHashMapGetValue(event.text, leadChannelState.value)
                    _mainData.update { it.copy(channelId = id) }
                }
                is UIEvent.OnLeadMediaChanged -> {
                    selectedDropdownItem.update { it.copy(leadMedia = event.text) }
                    val id = settingsHashMapGetValue(event.text, leadMediaState.value)
                    _mainData.update { it.copy(mediaId = id) }
                }
                is UIEvent.OnLeadSourceChanged -> {
                    selectedDropdownItem.update { it.copy(leadSource = event.text) }
                    val id = settingsHashMapGetValue(event.text, leadSourceState.value)
                    _mainData.update { it.copy(sourceId = id) }
                }
                is UIEvent.OnLeadStatusChanged -> {
                    selectedDropdownItem.update { it.copy(leadStatus = event.text) }
                    val id = settingsHashMapGetValue(event.text, leadStatusState.value)
                    _mainData.update { it.copy(statusId = id) }
                }
                is UIEvent.OnLeadProbabilityChanged -> {
                    selectedDropdownItem.update { it.copy(leadProbability = event.text) }
                    val id = settingsHashMapGetValue(event.text, leadProbabilityState.value)
                    _mainData.update { it.copy(probabilityId = id) }
                }
                is UIEvent.OnNotesChanged -> {
                    _mainData.update { it.copy(notes = event.text) }
                }
                is UIEvent.OnCreateLead -> {
                    _createLeadState.emit(Resource.Loading())
                    _createLeadState.emit(mainRepository.createLead(mainData.value))
                }
            }
        }
        validateInput()
    }

    private fun settingsHashMapGetValue(
        key: String,
        leadState: Resource<HashMap<String, Int>>
    ): Int {
        return when(leadState) {
            is Resource.Loading -> 0
            is Resource.Success -> {
                leadState.data?.let { it[key] } ?: 0
            }
            else -> -1
        }
    }

    private fun validateInput() {
        val data = mainData.value
        val isNextButtonValid: Boolean = with(data) {
            (branchOfficeId != 0) &&
                    fullName.isNotEmpty() &&
                    email.isValidEmail() &&
                    phone.isNotEmpty() &&
                    prefixPhone.isNotEmpty() &&
                    address.isNotEmpty() &&
                    latitude.isNotEmpty() &&
                    longitude.isNotEmpty() &&
                    gender.isNotEmpty() &&
                    idNumber.isNotEmpty() &&
                    (idNumberPhoto != null) &&
                    idNumberPhoto.isFile
        }

        val isSubmitButtonValid: Boolean = with(data) {
            isNextButtonValid &&
                    typeId != 0 &&
                    channelId != 0 &&
                    mediaId != 0 &&
                    sourceId != 0 &&
                    statusId != 0 &&
                    probabilityId != 0 &&
                    notes.isNotEmpty()
        }

        if (isNextButtonValid) {
            updateButtonNextState(true)
        } else {
            updateButtonNextState(false)
        }

        if (isSubmitButtonValid) {
            updateButtonSubmitState(true)
        } else {
            updateButtonSubmitState(false)
        }
    }

    private fun updateButtonNextState(isEnable: Boolean) {
        viewModelScope.launch {
            _buttonNextState.emit(isEnable)
        }
    }
    private fun updateButtonSubmitState(isEnable: Boolean) {
        viewModelScope.launch {
            _buttonSubmitState.emit(isEnable)
        }
    }

    private fun initialized() = CreateLeadData(
        branchOfficeId = 0,
        probabilityId = 0,
        typeId = 0,
        channelId = 0,
        mediaId = 0,
        sourceId = 0,
        statusId = 0,
        fullName = "",
        email = "",
        prefixPhone = "+62",
        phone = "",
        address = "",
        latitude = "",
        longitude = "",
        companyName = "Global Xtreme",
        gender = "",
        idNumber = "",
        idNumberPhoto = null,
        notes = ""
    )

    private fun selectedDropdownItem() = SelectedDropdownItem(
        branchOffice = "",
        prefixPhone = "+62",
        leadType = "",
        leadChannel = "",
        leadMedia = "",
        leadSource = "",
        leadStatus = "",
        leadProbability = ""
    )

    data class SelectedDropdownItem(
        var branchOffice: String,
        var leadType: String,
        var leadChannel: String,
        var leadMedia: String,
        var leadSource: String,
        var leadStatus: String,
        var leadProbability: String,
        var prefixPhone: String
    )

    sealed interface UIEvent {
        data class OnBranchOfficeChanged(val text: String): UIEvent
        data class OnFullNameChanged(val text: String): UIEvent
        data class OnEmailChanged(val text: String): UIEvent
        data class OnPrefixPhoneChanged(val text: String): UIEvent
        data class OnPhoneChanged(val text: String): UIEvent
        data class OnLocationChanged(
            val address: String,
            val latitude: String,
            val longitude: String
        ): UIEvent
        data class OnAddressChanged(val text: String): UIEvent
        data class OnGenderChanged(val text: String): UIEvent
        data class OnIdentityNumberChanged(val text: String): UIEvent
        data class OnImageIDNumberChanged(val file: File?): UIEvent
        data class OnLeadTypeChanged(val text: String): UIEvent
        data class OnLeadChannelChanged(val text: String): UIEvent
        data class OnLeadMediaChanged(val text: String): UIEvent
        data class OnLeadSourceChanged(val text: String): UIEvent
        data class OnLeadStatusChanged(val text: String): UIEvent
        data class OnLeadProbabilityChanged(val text: String): UIEvent
        data class OnNotesChanged(val text: String): UIEvent
        data object OnCreateLead: UIEvent
    }
}