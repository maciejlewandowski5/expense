package com.example.e.login.session

import com.example.e.fetchObject
import com.example.e.login.remote.RefreshRequest
import com.example.e.sharedpreferences.Preferences
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalSerializationApi
class SessionStore @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val authService: AuthService,
    private val preferences: Preferences,
    private val tokenRepository: TokenRepository
) {

    private var expiryHandlingJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    suspend fun updateTokens(accessToken: AccessToken, refreshToken: String) {
        setNewTokens(accessToken, refreshToken)
    }

    fun clearTokens() {
        setAccessToken(null)
    }

    private suspend fun setNewTokens(accessToken: AccessToken?, refreshToken: String?) {
        preferences.setRefreshToken(refreshToken)
        setAccessToken(accessToken)
    }

    private fun setAccessToken(newToken: AccessToken?) {
        tokenRepository._accessToken.postValue(newToken)
        expiryHandlingJob = newToken?.let { onNewAccessToken(it.timeToLeave) }
    }

    private fun onNewAccessToken(expiresIn: Long) = coroutineScope.launch {
        delay(TimeUnit.SECONDS.toMillis(expiresIn) - BEFORE_EXPIRATION)

        preferences.getRefreshToken()?.let {
            fetchObject { authService.refreshToken(RefreshRequest(it)) }
                .fold({ goToLoginScreen() }, { refreshedSuccessfully(it) })
        }
    }

    private suspend fun refreshedSuccessfully(it: LoginData) {
        preferences.setRefreshToken(it.refreshToken)
        setAccessToken(it.toAccessToken())
    }

    private fun goToLoginScreen() {
        setAccessToken(null)
    }

    private companion object {
        const val BEFORE_EXPIRATION = 1000L * 120
    }
}
