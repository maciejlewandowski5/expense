package com.example.e.settle

import com.example.e.data.repository.RepositoryImpl
import com.example.e.domain.Expense
import com.example.e.domain.Expense.Companion.splitExpense
import com.example.e.sharedpreferences.Preferences
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.Clock
import javax.inject.Inject

@ExperimentalSerializationApi
class SettleUseCase @Inject constructor(
    private val preferences: Preferences,
    val repositoryImpl: RepositoryImpl,
    private val clock: Clock
) {
    fun singleSettlementPolicy() = preferences.isSingleSettlementPolicy()

    suspend fun getSingleExpense(payOffText: String): Expense? =
        repositoryImpl.groupDetails(preferences.groupId()).fold({ null }, {
            it.getPayOffExpense(
                payOffText,
                clock
            )
        })

    suspend fun getMultipleExpenses(payOffText: String): List<Expense> =
        getSingleExpense(payOffText)?.splitExpense(clock) ?: emptyList()

    suspend fun setPayOffPolicy(value: Boolean) = preferences.setSingleSettlementPolicy(value)

    suspend fun addExpense(expense: Expense) =
        repositoryImpl.addExpense(expense, preferences.groupId())
}
