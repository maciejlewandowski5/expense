package com.example.e.sharedpreferences

import android.content.SharedPreferences
import com.example.e.domain.GroupId
import com.example.e.main.MainViewModelUseCase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Preferences @Inject constructor(private val sharedPreferences: SharedPreferences) {
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

    fun isSingleSettlementPolicy(): Boolean = sharedPreferences.getBoolean(SETTLEMENT_POLICY, false)

    companion object {
        const val DEFAULT_GROUP = "DEFAULT_GROUP"
        private const val SETTLEMENT_POLICY = "SINGLE_SETTLEMENT_POLICY"
    }
}
