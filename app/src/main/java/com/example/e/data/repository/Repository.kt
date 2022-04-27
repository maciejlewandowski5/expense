package com.example.e.data.repository

import arrow.core.Validated
import com.example.e.domain.*

interface Repository {

    suspend fun allGroups(): Validated<Throwable, AllGroups>

    suspend fun groupDetails(groupId: GroupId): Validated<Throwable, GroupDetails>

    suspend fun groupUsers(groupId: GroupId): Validated<Throwable, List<User>>

    suspend fun addExpense(expense: Expense, groupId: GroupId): Validated<Throwable, Long>

    suspend fun addUser(user: User): Validated<Throwable, Long>

    suspend fun allUsers(): Validated<Throwable, List<User>>

    suspend fun addMember(user: User, groupId: GroupId): Validated<Throwable, Long>

    suspend fun addGroup(
        accountingGroup: AccountingGroup,
        members: List<User>
    ): Validated<Throwable, GroupId>
}
