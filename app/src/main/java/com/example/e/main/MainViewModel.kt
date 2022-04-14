package com.example.e.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e.domain.AccountingGroup
import com.example.e.expense.ExpensesState
import com.example.e.main.spendings.CurrentSpendingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: MainViewModelUseCase) :
    ViewModel() {
    private val _currentSpending: MutableLiveData<CurrentSpendingState> =
        MutableLiveData(CurrentSpendingState.Loading)
    val currentSpending: LiveData<CurrentSpendingState> = _currentSpending

    private val _expensesState: MutableLiveData<ExpensesState> =
        MutableLiveData(ExpensesState.Loading)
    val expensesState: LiveData<ExpensesState> = _expensesState

    fun fetchExpenses() = viewModelScope.launch {
       // useCase.dummyData()
        useCase.fetchGroupDetails(_currentSpending, _expensesState)
    }

    fun setCurrentGroup(accountingGroup: AccountingGroup) = viewModelScope.launch {
        useCase.setCurrentGroup(accountingGroup)
        _expensesState.value = ExpensesState.Loading
        fetchExpenses()
    }
}
