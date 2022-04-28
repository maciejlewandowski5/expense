package com.example.e.source

import com.example.e.login.session.SessionStore
import com.example.e.login.session.TokenRepository
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class SwitchSourceUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val sessionStore: SessionStore
) {

    suspend fun switchSource(isSourceRemote: Boolean) {
        tokenRepository.setSourceRemote(isSourceRemote)
        sessionStore.clearTokens()
    }

    fun getSource(): Boolean = tokenRepository.isSourceRemote.value == true
}
