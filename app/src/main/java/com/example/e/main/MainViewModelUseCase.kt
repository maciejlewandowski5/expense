package com.example.e.main

import androidx.lifecycle.MutableLiveData
import com.example.e.data.Repository
import com.example.e.domain.AccountingGroup
import com.example.e.expense.ExpensesState
import com.example.e.logThrowable
import com.example.e.sharedpreferences.Preferences
import com.example.e.main.spendings.CurrentSpendingState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class MainViewModelUseCase @Inject constructor(
    private val repository: Repository,
    private val preferences: Preferences
) {

    suspend fun dummyData() = repository.dummyData()

    suspend fun fetchGroupDetails(
        currentSpendingState: MutableLiveData<CurrentSpendingState>,
        expensesState: MutableLiveData<ExpensesState>
    ) = try {
        groupDetails(expensesState, currentSpendingState)
    } catch (e: Throwable) {
        e.logThrowable(this)
        currentSpendingState.value = CurrentSpendingState.Error
        expensesState.value = ExpensesState.Error
    }

    private suspend fun groupDetails(
        expensesState: MutableLiveData<ExpensesState>,
        currentSpendingState: MutableLiveData<CurrentSpendingState>
    ) = coroutineScope {
        val detailsAsync = async { repository.groupDetails(preferences.groupId()) }
        val groups = async { repository.allGroups() }
        val details = detailsAsync.await()
        expensesState.value = ExpensesState.Success(details.expenses.reversed())

        currentSpendingState.value =
            CurrentSpendingState.Success(details.toHeaderCardData(groups.await()))
    }

    suspend fun setCurrentGroup(accountingGroup: AccountingGroup) =
        preferences.setGroupId(accountingGroup.id)

    companion object {
        const val DEFAULT_GROUP = "DEFAULT_GROUP"
    }
}
