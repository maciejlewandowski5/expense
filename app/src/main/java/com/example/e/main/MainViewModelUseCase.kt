package com.example.e.main

import androidx.lifecycle.MutableLiveData
import arrow.core.andThen
import arrow.core.valid
import com.example.e.data.repository.RepositoryImpl
import com.example.e.domain.AccountingGroup
import com.example.e.domain.Expense
import com.example.e.expense.ExpensesState
import com.example.e.logThrowable
import com.example.e.main.spendings.CurrentSpendingState
import com.example.e.sharedpreferences.Preferences
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class MainViewModelUseCase @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    private val preferences: Preferences,
) {

    suspend fun fetchGroupDetails(
        currentSpendingState: MutableLiveData<CurrentSpendingState>,
        expensesState: MutableLiveData<ExpensesState>,
    ) = groupDetails(expensesState, currentSpendingState)

    private suspend fun groupDetails(
        expensesState: MutableLiveData<ExpensesState>,
        currentSpendingState: MutableLiveData<CurrentSpendingState>
    ) = coroutineScope {
        val detailsAsync = async { repositoryImpl.groupDetails(preferences.groupId()) }
        val groups = async { repositoryImpl.allGroups() }

        detailsAsync.await().andThen { group ->
            expensesState.value =
                ExpensesState.Success(group.expenses.sortedByDescending { it.date })
            groups.await().andThen {
                group.toHeaderCardData(it.groups).valid()
            }
        }.fold({
            it.logThrowable(this)
            expensesState.value = ExpensesState.Error
            currentSpendingState.value = CurrentSpendingState.Error
        }, {
            currentSpendingState.value = CurrentSpendingState.Success(it)
        })
    }

    suspend fun setCurrentGroup(accountingGroup: AccountingGroup) =
        preferences.setGroupId(accountingGroup.id)

    suspend fun deleteExpense(expense: Expense) {
        repositoryImpl.deleteExpense(expense, preferences.groupId())
    }

    suspend fun addExpense(expense: Expense) {
        repositoryImpl.addExpense(expense,preferences.groupId())
    }

    companion object {
        const val DEFAULT_GROUP = "DEFAULT_GROUP"
    }
}
