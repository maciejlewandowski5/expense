package com.example.e.expense

import com.example.e.domain.Expense

sealed class ExpensesState {
    object Loading : ExpensesState()
    class Success(val expenses: List<Expense>) : ExpensesState()
    object Error : ExpensesState()
}
