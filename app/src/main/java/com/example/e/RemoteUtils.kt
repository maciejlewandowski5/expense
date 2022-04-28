package com.example.e

import android.accounts.NetworkErrorException
import arrow.core.*
import com.example.e.domain.DomainModel
import com.example.e.domain.ResponseModel
import retrofit2.Response

internal suspend fun <T : ResponseModel<G>, G : DomainModel> fetchList(apiCall: suspend () -> Response<List<T>>) =
    tryAsValidated(apiCall) { map { it.toDomain() } }

suspend fun <T : ResponseModel<G>, G : DomainModel> fetchObject(apiCall: suspend () -> Response<T>) =
    tryAsValidated(apiCall) { toDomain() }

internal suspend fun addObject(apiCall: suspend () -> Response<Long>) =
    tryAsValidated(apiCall) { this }

private suspend fun <T : Any, G : Any> tryAsValidated(
    fetchingFunction: suspend () -> Response<T>,
    conversionFunction: suspend T.() -> G
): Validated<Throwable, G> = try {
    val response = fetchingFunction.invoke()
    if (response.isSuccessful) {
        response.body()?.conversionFunction()?.valid() ?: NetworkErrorException().invalid()
    } else {
        NetworkErrorException().invalid()
    }
} catch (e: Throwable) {
    e.invalid()
}
