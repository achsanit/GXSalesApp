package com.achsanit.gxsales.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    var data = initialized()

    fun dispatchEvent(event: UiEvent) {
        viewModelScope.launch {
            when(event) {
                is UiEvent.EmailChanged -> {
                    updateData(data.copy(email = event.text))
                }
                is UiEvent.PasswordChanged -> {
                    updateData(data.copy(password = event.text))
                }
                is UiEvent.SignIn -> {

                }
            }
        }
    }

    private fun updateData(signInData: SignInData) { data = signInData }

    fun isAbleToLogin(): Boolean {
        with(data) {
            return email.isNotBlank() && password.isNotBlank()
        }
    }

    private fun initialized() = SignInData(
        email = "",
        password = ""
    )

    data class SignInData(
        val email: String,
        val password: String
    )

    sealed interface UiEvent {
        data class EmailChanged(val text: String): UiEvent
        data class PasswordChanged(val text: String): UiEvent
        data object SignIn: UiEvent
    }
}