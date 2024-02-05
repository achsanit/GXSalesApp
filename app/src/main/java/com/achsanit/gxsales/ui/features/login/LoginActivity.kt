package com.achsanit.gxsales.ui.features.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.achsanit.gxsales.R
import com.achsanit.gxsales.databinding.ActivityLoginBinding
import com.achsanit.gxsales.utils.isValidEmail
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnTextChangeListener()

        with(binding) {
            btnSignIn.setOnClickListener { userLogin() }
        }
    }

    private fun userLogin() {
        if (viewModel.isAbleToLogin()) {
            //TODO: integrate with login API
        } else {
            with(viewModel.data) {
                showEmailAlert(!email.isValidEmail())
                showPasswordAlert(password.isBlank())
            }
            Snackbar.make(
                binding.root,
                getString(R.string.message_invalid_input),
                Snackbar.LENGTH_LONG
            ).setAction(getString(R.string.ok)) {}
                .setActionTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .setBackgroundTint(ContextCompat.getColor(this, R.color.red))
                .show()
        }
    }

    private fun setupOnTextChangeListener() {
        with(binding) {
            edtEmail.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatchEvent(LoginViewModel.UiEvent.EmailChanged(text.toString()))
                showEmailAlert(!text.isValidEmail())
            }
            edtPassword.doOnTextChanged { text, _, _, _ ->
                viewModel.dispatchEvent(LoginViewModel.UiEvent.PasswordChanged(text.toString()))
                showPasswordAlert(text.isNullOrBlank())
            }
        }
    }

    private fun showEmailAlert(isNotValid: Boolean) {
        if (isNotValid) {
            binding.tilEmail.error = "Invalid Email"
        } else {
            binding.tilEmail.apply {
                error = null
                isErrorEnabled = false
            }
        }
    }
    private fun showPasswordAlert(isNotValid: Boolean) {
        if (isNotValid) {
            binding.tilPassword.error = "Input can not be empty"
        } else {
            binding.tilPassword.apply {
                error = null
                isErrorEnabled = false
            }
        }
    }
}