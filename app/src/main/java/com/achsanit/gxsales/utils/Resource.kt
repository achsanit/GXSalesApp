package com.achsanit.gxsales.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null, val codeError: Int = 0) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, codeError: Int, data: T? = null) :
        Resource<T>(data, message, codeError)

    class ServerError<T>(message: String, codeError: Int, data: T? = null) :
        Resource<T>(data, message, codeError)

    // collect the Resource
    fun collect(
        onLoading: () -> Unit,
        onSuccess: (T?) -> Unit,
        onError: (String?, Int) -> Unit,
    ) {
        when(this) {
            is Loading -> onLoading()
            is Success -> onSuccess(data)
            else -> onError(message, codeError)
        }
    }
}