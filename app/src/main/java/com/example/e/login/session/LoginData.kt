package com.example.e.login.session

import com.example.e.domain.DomainModel

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val timeToLeave: Long
) : DomainModel {
    fun toAccessToken() = AccessToken(token = accessToken, timeToLeave = timeToLeave)
}
