package com.achsanit.gxsales.utils

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.achsanit.gxsales.R
import com.achsanit.gxsales.ui.dialog.SessionEndedDialogFragment
import com.achsanit.gxsales.ui.features.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import java.net.HttpURLConnection

fun CharSequence?.isValidEmail(): Boolean {
    return !isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun View.makeGone() {
    this.visibility = View.GONE
}
fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun Fragment.onNetworkEvent(code: Int, messageError: String? = null) {
    val message = messageError ?: when(code) {
        CodeError.NO_INTERNET_CONNECTION -> "Please check your internet connection and try again later..."
        CodeError.REQUEST_TIME_OUT -> "Request time out"
        else -> { "Something went wrong..." }
    }

    when(code) {
        HttpURLConnection.HTTP_UNAUTHORIZED -> {
            SessionEndedDialogFragment {
                context?.let {
                    val intent = Intent(it, LoginActivity::class.java)
                    it.startActivity(intent)
                    activity?.finish()
                }
            }
                .show(
                    parentFragmentManager,
                    SessionEndedDialogFragment.TAG
                )
        }
        else -> {
            Snackbar.make(
                requireView(),
                message,
                Snackbar.LENGTH_LONG
            ).setAction(getString(R.string.ok)) {}
                .setActionTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                .show()
        }
    }
}