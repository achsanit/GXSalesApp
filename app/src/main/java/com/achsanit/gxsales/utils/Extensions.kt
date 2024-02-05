package com.achsanit.gxsales.utils

import android.util.Patterns

fun CharSequence?.isValidEmail(): Boolean {
    return !isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}