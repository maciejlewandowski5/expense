package com.example.e.settle

import com.example.e.domain.Expense

sealed class SettlementExpenseState {
    object Loading : SettlementExpenseState()
    class Success(val expenses: List<Expense>) : SettlementExpenseState()
    class Error(val error: Throwable) : SettlementExpenseState()
}
