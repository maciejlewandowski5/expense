package com.example.e.main

import com.example.e.domain.AccountingGroup

interface MainViewContract {
    fun switchSource(isSourceRemote: Boolean): Unit?
    fun setCurrentGroup(accountingGroup: AccountingGroup): Unit?
    fun onRefresh(): Unit?
}
