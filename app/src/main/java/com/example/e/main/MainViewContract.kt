package com.example.e.main

import com.example.e.domain.AccountingGroup
import com.example.e.domain.Expense

interface MainViewContract {
    fun switchSource(isSourceRemote: Boolean): Unit?
    fun setCurrentGroup(accountingGroup: AccountingGroup): Unit?
    fun onRefresh(): Unit?
    fun deleteExpense(expense: Expense): Unit?
    fun cancelDelete(): Unit?
    fun setCurrentGroup(index: Int): Unit?
    fun getCurrentGroupIndex(): Int
    fun getPreviousGroupName(): String
    fun getNextGroupName(): String
}
