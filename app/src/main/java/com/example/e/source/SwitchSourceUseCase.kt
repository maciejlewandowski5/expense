package com.example.e.source

import com.example.e.login.session.SessionStore
import com.example.e.sharedpreferences.Preferences
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class SwitchSourceUseCase @Inject constructor(
    private val preferences: Preferences,
    private val sessionStore: SessionStore
) {

    suspend fun switchSource(isSourceRemote: Boolean) {
        preferences.setSourceRemote(isSourceRemote)
        sessionStore.clearTokens()
    }

    fun getSource(): Boolean = preferences.isSourceRemote()
}
