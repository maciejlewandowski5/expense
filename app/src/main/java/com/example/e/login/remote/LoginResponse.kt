package com.example.e.login.remote

import com.example.e.domain.ResponseModel
import com.example.e.login.session.AccessToken
import com.example.e.login.session.LoginData
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val ttl: Long
) : ResponseModel<LoginData> {
    override fun toDomain() =
        LoginData(accessToken = accessToken, refreshToken = refreshToken, timeToLeave = ttl)
}

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)
