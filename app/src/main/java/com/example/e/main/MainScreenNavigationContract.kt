package com.example.e.main

interface MainScreenNavigationContract {
    fun goToAddExpense(): Unit?
    fun menuAction(): Unit?
    fun goToSettlement(): Unit?
    fun goToAddNewGroup(): Unit?
}
