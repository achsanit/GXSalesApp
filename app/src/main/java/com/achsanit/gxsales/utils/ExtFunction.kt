package com.achsanit.gxsales.utils

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.location.Geocoder
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.achsanit.gxsales.R
import com.achsanit.gxsales.ui.dialog.SessionEndedDialogFragment
import com.achsanit.gxsales.ui.features.login.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.util.Locale

// extension for checking email is valid or not
fun CharSequence?.isValidEmail(): Boolean {
    return !isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// extension for set visibility of view to gone, visible, and invisible
fun View.makeGone() {
    this.visibility = View.GONE
}
fun View.makeVisible() {
    this.visibility = View.VISIBLE
}
fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

// to handling the network error by code and message error
fun Fragment.onNetworkEvent(code: Int, messageError: String? = null) {
    val message = messageError ?: when(code) {
        CodeError.NO_INTERNET_CONNECTION -> "Please check your internet connection and try again later..."
        CodeError.REQUEST_TIME_OUT -> "Request time out"
        else -> { "Something went wrong..." }
    }

    when(code) {
        // when error unauthorized show dialog for return to login
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

// extension for get size of file in kb
fun File.getSizeKb(): Long = this.length() / 1024

// to get an address of latLng
fun Fragment.getAddress(lat: Double, lng: Double): String {
    val geocoder = Geocoder(requireActivity().applicationContext, Locale.getDefault())
    return try {
        // TODO : This code is deprecated, need to change to another code
        geocoder.getFromLocation(lat, lng, 1)!!.first().getAddressLine(0)
    } catch (e: IOException) {
        e.printStackTrace()
        "-"
    } catch (e: NullPointerException) {
        e.printStackTrace()
        "-"
    }
}

// extension for set state of button
fun MaterialButton.setEnable(context: Context, state: Boolean) {
    if (state) {
        // when enable set background and text color to primary color
        // and set enable button to true
        this.apply {
            isEnabled = true
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.primary_yellow)
            )
            setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    } else {
        // when disable set background and text color to disable color
        // and set enable button to false
        this.apply {
            isEnabled = false
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.gray_300)
            )
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }
}