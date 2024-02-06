package com.achsanit.gxsales.ui.features.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.achsanit.gxsales.BuildConfig
import com.achsanit.gxsales.R
import com.achsanit.gxsales.data.response.LoginResponse
import com.achsanit.gxsales.databinding.ActivityLoginBinding
import com.achsanit.gxsales.ui.features.main.MainActivity
import com.achsanit.gxsales.utils.Resource
import com.achsanit.gxsales.utils.Statics
import com.achsanit.gxsales.utils.isValidEmail
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeVisible
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnTextChangeListener()
        setUpStateListener()

        with(binding) {
            btnSignIn.setOnClickListener { userLogin() }
            textAppVersion.text =
                resources.getString(R.string.text_version_placeholder, BuildConfig.VERSION_NAME)
        }
    }

    private fun setUpStateListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.loginState.collect(::loginStateListener)
            }
        }
    }

    private fun loginStateListener(data: Resource<LoginResponse>) {
        with(binding) {
            when (data) {
                is Resource.Loading -> {
                    pbLogin.makeVisible()
                }

                is Resource.Success -> {
                    pbLogin.makeGone()

                    if (!(data.data?.token.isNullOrEmpty())) {
                        // TODO: save token to data store pref
                    }

                    if (data.data?.status.equals(Statics.SUCCESS)) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                else -> {
                    pbLogin.makeGone()

                    val message = when (data.codeError) {
                        401 -> resources.getString(R.string.message_check_email_password)
                        else -> resources.getString(
                            R.string.something_went_wrong_placeholder,
                            data.codeError.toString()
                        )
                    }

                    Snackbar.make(
                        binding.root,
                        message,
                        Snackbar.LENGTH_LONG
                    ).setAction(getString(R.string.ok)) {}
                        .setActionTextColor(
                            ContextCompat.getColor(
                                this@LoginActivity,
                                R.color.white
                            )
                        )
                        .setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
                        .setBackgroundTint(ContextCompat.getColor(this@LoginActivity, R.color.red))
                        .show()
                }
            }
        }
    }

    private fun userLogin() {
        if (viewModel.isAbleToLogin()) {
            viewModel.dispatchEvent(LoginViewModel.UiEvent.SignIn)
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