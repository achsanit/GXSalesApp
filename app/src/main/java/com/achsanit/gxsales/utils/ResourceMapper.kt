package com.achsanit.gxsales.utils

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <T> resourceMapper(
    crossinline code: suspend () -> T
): Resource<T> {
    return try {
        // return T as resource success
        Resource.Success(code.invoke())
    } catch (httpException: HttpException) {
        when (httpException.code()) {
            in 400..499 -> {
                Resource.Error(
                    httpException.message.toString() + "-${httpException.code()}",
                    httpException.code()
                )
            }
            in 500..599 -> {
                Resource.ServerError(
                    httpException.message.toString() + "-${httpException.code()}",
                    httpException.code()
                )
            }
            else -> {
                Resource.Error(
                    httpException.message.toString() + "-${httpException.code()}",
                    httpException.code(),
                )
            }
        }
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        Resource.Error(e.message.toString(), -1)
    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        Resource.Error(e.message.toString(), 0)
    } catch (e: Exception) {
        Resource.Error(e.message.toString(), 2)
    }
}
