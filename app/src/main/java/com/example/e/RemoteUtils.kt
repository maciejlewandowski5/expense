package com.example.e.data

import android.accounts.NetworkErrorException
import arrow.core.*
import com.example.e.domain.DomainModel
import com.example.e.domain.ResponseModel
import retrofit2.Response

internal suspend fun <T : ResponseModel<G>, G : DomainModel> fetchList(
    fetchingFunction: suspend () -> Response<List<T>>
): Validated<Throwable, List<G>> = try {
    val response = fetchingFunction.invoke()
    if (response.isSuccessful) {
        response.body()?.map { it.toDomain() }?.valid() ?: NetworkErrorException().invalid()
    } else {
        NetworkErrorException().invalid()
    }
} catch (e: Throwable) {
    e.invalid()
}

suspend fun <T : ResponseModel<G>, G : DomainModel> fetchObject(
    fetchingFunction: suspend () -> Response<T>
): Validated<Throwable, G> = try {
    val response = fetchingFunction.invoke()
    if (response.isSuccessful) {
        response.body()?.toDomain()?.valid() ?: NetworkErrorException().invalid()
    } else {
        NetworkErrorException().invalid()
    }
} catch (e: Throwable) {
    e.invalid()
}

internal suspend fun addObject(
    fetchingFunction: suspend () -> Response<Long>
): Validated<Throwable, Long> = try {
    val response = fetchingFunction.invoke()
    if (response.isSuccessful) {
        response.body()?.valid() ?: NetworkErrorException().invalid()
    } else {
        NetworkErrorException().invalid()
    }
} catch (e: Throwable) {
    e.invalid()
}
