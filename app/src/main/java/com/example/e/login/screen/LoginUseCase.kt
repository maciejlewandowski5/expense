package com.example.e.login.screen

import com.example.e.data.fetchObject
import com.example.e.login.remote.LoginRequest
import com.example.e.login.session.AuthService
import com.example.e.login.session.SessionStore
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class LoginUseCase
@Inject constructor(
    private val service: AuthService,
    private val sessionStore: SessionStore
) {
    suspend fun login(userName: String, password: String) =
        fetchObject { service.login(LoginRequest(username = userName, password = password)) }
            .tap { sessionStore.updateTokens(it.toAccessToken(), it.refreshToken) }
            .fold({ LoginEffect.ShowErrorMessage(it) }, { LoginEffect.GoToMainScreen })
}
