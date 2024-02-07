package com.achsanit.gxsales.utils

import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.achsanit.gxsales.R
import com.google.android.material.snackbar.Snackbar

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