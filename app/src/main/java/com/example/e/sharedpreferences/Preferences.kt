package com.example.e.sharedpreferences

import android.content.SharedPreferences
import com.example.e.domain.GroupId
import com.example.e.main.MainViewModelUseCase
import com.example.e.sharedpreferences.PreferencesModule.Companion.ENCRYPTED_PREFERENCES
import com.example.e.sharedpreferences.PreferencesModule.Companion.SHARED_PREFERENCES
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Preferences @Inject constructor(
    @Named(SHARED_PREFERENCES)
    private val sharedPreferences: SharedPreferences,
    @Named(ENCRYPTED_PREFERENCES)
    private val encryptedSharedPreferences: SharedPreferences
) {
    fun groupId(): GroupId = GroupId(sharedPreferences.getLong(DEFAULT_GROUP, 1))

    suspend fun setGroupId(newGroupId: GroupId) = suspendCoroutine<Unit> { cont ->
        sharedPreferences.edit().putLong(MainViewModelUseCase.DEFAULT_GROUP, newGroupId.value)
            .apply()
            .apply { cont.resume(Unit) }
    }

    suspend fun setSingleSettlementPolicy(newValue: Boolean) = suspendCoroutine<Unit> { cont ->
        sharedPreferences.edit().putBoolean(SETTLEMENT_POLICY, newValue)
            .apply()
            .apply { cont.resume(Unit) }
    }

    suspend fun setSourceRemote(newValue: Boolean) = suspendCoroutine<Unit> { cont ->
        sharedPreferences.edit().putBoolean(SOURCE, newValue)
            .apply()
            .apply { cont.resume(Unit) }
    }

    suspend fun setRefreshToken(token: String?) = suspendCoroutine<Unit> { cont ->
        encryptedSharedPreferences.edit().putString(REFRESH_TOKEN, token).apply().apply {
            cont.resume(Unit)
        }
    }

    fun getRefreshToken(): String? = encryptedSharedPreferences.getString(REFRESH_TOKEN, null)

    fun isSingleSettlementPolicy(): Boolean = sharedPreferences.getBoolean(SETTLEMENT_POLICY, false)

    fun isSourceRemote(): Boolean = sharedPreferences.getBoolean(SOURCE, false)

    companion object {
        const val DEFAULT_GROUP = "DEFAULT_GROUP"
        private const val SETTLEMENT_POLICY = "SINGLE_SETTLEMENT_POLICY"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val SOURCE = "SOURCE"
    }
}
