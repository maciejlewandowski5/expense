package com.example.e.settle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e.domain.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettleViewModel @Inject constructor(val settleUseCase: SettleUseCase) : ViewModel() {

    private val _isSettlePolicyASummary: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSettlePolicyASummary: LiveData<Boolean> = _isSettlePolicyASummary

    private val _settlementExpenses: MutableLiveData<SettlementExpenseState> =
        MutableLiveData(SettlementExpenseState.Loading)
    val settlementExpenses: LiveData<SettlementExpenseState> = _settlementExpenses

    fun init(payOffText: String) {
        viewModelScope.launch {
            _isSettlePolicyASummary.value = settleUseCase.singleSettlementPolicy()
            if (_isSettlePolicyASummary.value == true) {
                _settlementExpenses.value =
                    SettlementExpenseState.Success(
                        settleUseCase.getSingleExpense(payOffText)?.let { listOf(it) }
                            ?: emptyList()
                    )
            } else {
                _settlementExpenses.value =
                    SettlementExpenseState.Success(settleUseCase.getMultipleExpenses(payOffText))
            }
        }
    }

    fun setPayOffPolicy(value: Boolean, payOffText: String) {
        viewModelScope.launch {
            _isSettlePolicyASummary.value = value
            _settlementExpenses.value = SettlementExpenseState.Loading
            settleUseCase.setPayOffPolicy(value)
        }
        init(payOffText)
    }

    fun addExpense(expense: Expense) {
        if (_settlementExpenses.value is SettlementExpenseState.Success && _settlementExpenses.value != null) {
            viewModelScope.launch {
                settleUseCase.addExpense(expense)
                val expenses =
                    (_settlementExpenses.value as SettlementExpenseState.Success).expenses.toMutableList()
                expenses.remove(expense)
                _settlementExpenses.value = SettlementExpenseState.Success(expenses)
            }
        }
    }
}
