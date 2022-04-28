package com.example.e.login.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.e.sharedpreferences.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(val preferences: Preferences) {
    internal val _accessToken: MutableLiveData<AccessToken?> = MutableLiveData(null)
    val accessToken: LiveData<AccessToken?> = _accessToken

    private val _isSourceRemote: MutableLiveData<Boolean> =
        MutableLiveData(preferences.isSourceRemote())
    val isSourceRemote: LiveData<Boolean> = _isSourceRemote

    suspend fun setSourceRemote(isSourceRemote: Boolean) {
        _isSourceRemote.value = isSourceRemote
        preferences.setSourceRemote(isSourceRemote)
    }
}
