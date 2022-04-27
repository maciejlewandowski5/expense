package com.example.e.login.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor() {
    internal val _accessToken: MutableLiveData<AccessToken?> = MutableLiveData(null)
    val accessToken: LiveData<AccessToken?> = _accessToken
}
