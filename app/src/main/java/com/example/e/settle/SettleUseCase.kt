package com.example.e.settle

import com.example.e.data.Repository
import com.example.e.domain.Expense
import com.example.e.domain.Expense.Companion.splitExpense
import com.example.e.sharedpreferences.Preferences
import java.time.Clock
import javax.inject.Inject

class SettleUseCase @Inject constructor(
    private val preferences: Preferences,
    val repository: Repository,
    private val clock: Clock
) {
    fun singleSettlementPolicy() = preferences.isSingleSettlementPolicy()

    suspend fun getSingleExpense(payOffText: String): Expense? =
        repository.groupDetails(preferences.groupId()).getPayOffExpense(payOffText, clock)

    suspend fun getMultipleExpenses(payOffText: String): List<Expense> =
        getSingleExpense(payOffText)?.splitExpense(clock) ?: emptyList()

    suspend fun setPayOffPolicy(value: Boolean) = preferences.setSingleSettlementPolicy(value)

    suspend fun addExpense(expense: Expense) = repository.addExpense(expense, preferences.groupId())
}
