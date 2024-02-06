package com.achsanit.gxsales.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.data.response.LoginResponse
import com.achsanit.gxsales.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val mainRepo: MainRepository): ViewModel() {

    var data = initialized()

    private val _loginState = MutableSharedFlow<Resource<LoginResponse>>()
    val loginState = _loginState.asSharedFlow()

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

    private fun updateLoginState(state: Resource<LoginResponse>) {
        viewModelScope.launch { _loginState.emit(state) }
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