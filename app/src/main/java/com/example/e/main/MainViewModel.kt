package com.example.e.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e.domain.AccountingGroup
import com.example.e.expense.ExpensesState
import com.example.e.main.spendings.CurrentSpendingState
import com.example.e.source.SwitchSourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun fetchExpenses() = viewModelScope.launch {
        useCase.fetchGroupDetails(_currentSpending, _expensesState)
        _isSourceRemote.value = switchSourceUseCase.getSource()
    }

    override fun setCurrentGroup(accountingGroup: AccountingGroup) {
        viewModelScope.launch {
            useCase.setCurrentGroup(accountingGroup)
            _expensesState.value = ExpensesState.Loading
            fetchExpenses()
        }
    }

    override fun switchSource(isSourceRemote: Boolean) {
        _isSourceRemote.value = isSourceRemote
        viewModelScope.launch {
            switchSourceUseCase.switchSource(isSourceRemote)
            fetchExpenses()
        }
    }
}
