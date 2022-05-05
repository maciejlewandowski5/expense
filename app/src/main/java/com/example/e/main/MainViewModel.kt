package com.example.e.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e.domain.AccountingGroup
import com.example.e.domain.Expense
import com.example.e.expense.ExpensesState
import com.example.e.main.spendings.CurrentSpendingState
import com.example.e.source.SwitchSourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalSerializationApi
class MainViewModel @Inject constructor(
    private val useCase: MainViewModelUseCase,
    private val switchSourceUseCase: SwitchSourceUseCase
) :
    ViewModel(), MainViewContract {
    private val _currentSpending: MutableLiveData<CurrentSpendingState> =
        MutableLiveData(CurrentSpendingState.Loading)
    val currentSpending: LiveData<CurrentSpendingState> = _currentSpending

    private val _expensesState: MutableLiveData<ExpensesState> =
        MutableLiveData(ExpensesState.Loading)
    val expensesState: LiveData<ExpensesState> = _expensesState

    private val _isSourceRemote: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSourceRemote: LiveData<Boolean> = _isSourceRemote

    private val _isRefreshingExpenses: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRefreshingExpenses: LiveData<Boolean> = _isRefreshingExpenses

    private val _deleteAction: MutableLiveData<Job?> = MutableLiveData(null)
    val deleteAction: LiveData<Job?> = _deleteAction

    private val _deleteTimeout: MutableLiveData<Int> = MutableLiveData(0)
    val deleteTimeout: LiveData<Int> = _deleteTimeout

    fun fetchExpenses() = viewModelScope.launch {
        _isRefreshingExpenses.value = true
        useCase.fetchGroupDetails(_currentSpending, _expensesState)
        _isSourceRemote.value = switchSourceUseCase.getSource()
        _isRefreshingExpenses.value = false
    }

    override fun setCurrentGroup(accountingGroup: AccountingGroup) {
        viewModelScope.launch {
            useCase.setCurrentGroup(accountingGroup)
            _expensesState.value = ExpensesState.Loading
            fetchExpenses()
        }
    }

    override fun onRefresh() {
        _isRefreshingExpenses.value = true
        fetchExpenses()
    }

    override fun deleteExpense(expense: Expense) {
        if (expensesState.value is ExpensesState.Success) {
            val list = (expensesState.value as ExpensesState.Success).expenses.toMutableList()
            _expensesState.value = ExpensesState.Loading
            list.remove(expense)
            _expensesState.value =
                ExpensesState.Success(list)
            _deleteAction.value = viewModelScope.launch {
                updateDeleteBarAndWait()
                useCase.deleteExpense(expense)
            }
            _deleteAction.value?.invokeOnCompletion {
                if (it is CancellationException) {
                    _expensesState.value =
                        ExpensesState.Success(
                            (_expensesState.value as ExpensesState.Success).expenses.toMutableList()
                                .also {
                                    it.add(expense)
                                    it.sortByDescending { it.date }
                                }.toList()
                        )
                }
            }
        }
    }

    override fun cancelDelete() {
        _deleteAction.value?.cancel()
        _deleteAction.value = null
        _deleteTimeout.value = 0
    }

    private suspend fun updateDeleteBarAndWait() {
        for (i in 1..500) {
            _deleteTimeout.value = _deleteTimeout.value?.plus(1)
            delay(1)
        }
        _deleteTimeout.value = 0
    }

    override fun switchSource(isSourceRemote: Boolean) {
        _isSourceRemote.value = isSourceRemote
        viewModelScope.launch {
            switchSourceUseCase.switchSource(isSourceRemote)
            fetchExpenses()
        }
    }
}
