package com.achsanit.gxsales.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.data.network.response.LoginResponse
import com.achsanit.gxsales.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val mainRepo: MainRepository): ViewModel() {

    var data = initialized() // default data is SignInData with empty for each attribute

    // State for action login
    private val _loginState = MutableSharedFlow<Resource<LoginResponse>>()
    val loginState = _loginState.asSharedFlow()

    fun dispatchEvent(event: UiEvent) {
        viewModelScope.launch {
            // handling all event from UiEvent
            when(event) {
                is UiEvent.EmailChanged -> {
                    updateData(data.copy(email = event.text))
                }
                is UiEvent.PasswordChanged -> {
                    updateData(data.copy(password = event.text))
                }
                is UiEvent.SignIn -> {
                    updateLoginState(Resource.Loading())
                    try {
                        updateLoginState(mainRepo.login(data.email, data.password))
                    } catch (e: Exception) {
                        updateLoginState(Resource.Error(e.message.toString(), -1))
                    }
                }
            }
        }
    }

    private fun updateLoginState(state: Resource<LoginResponse>) { // to update login state
        viewModelScope.launch { _loginState.emit(state) }
    }

    private fun updateData(signInData: SignInData) { data = signInData } //to update data state

    // save token to data store preference as a local store
    fun saveTokenUser(token: String) {
        viewModelScope.launch { mainRepo.saveLoginData(token) }
    }

    // function for checking user is able to login or not
    // this function to check input user is valid or not
    fun isAbleToLogin(): Boolean {
        with(data) {
            return email.isNotBlank() && password.isNotBlank()
        }
    }

    // default data is empty string
    private fun initialized() = SignInData(
        email = "",
        password = ""
    )

    // data class for login
    data class SignInData(
        val email: String,
        val password: String
    )

    // sealed interface for handle all Event from UI
    sealed interface UiEvent {
        data class EmailChanged(val text: String): UiEvent // state when email changed
        data class PasswordChanged(val text: String): UiEvent // state when password changed
        data object SignIn: UiEvent // state for user login
    }
}