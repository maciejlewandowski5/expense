package com.example.e.data.repository

import com.example.e.data.repository.local.LocalDataProvider
import com.example.e.data.repository.remote.RemoteDataProvider
import com.example.e.domain.*
import com.example.e.sharedpreferences.Preferences
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class RepositoryImpl @Inject constructor(
    private val localDataProvider: LocalDataProvider,
    private val repositoryRemoteProvider: RemoteDataProvider,
    private val preferences: Preferences
) : Repository {
    override suspend fun allGroups() = resolveRepository().allGroups()

    override suspend fun groupDetails(groupId: GroupId) = resolveRepository().groupDetails(groupId)

    override suspend fun groupUsers(groupId: GroupId) = resolveRepository().groupUsers(groupId)

    override suspend fun addExpense(expense: Expense, groupId: GroupId) =
        resolveRepository().addExpense(expense, groupId)

    override suspend fun addUser(user: User) = resolveRepository().addUser(user)

    override suspend fun allUsers() = resolveRepository().allUsers()

    override suspend fun addMember(user: User, groupId: GroupId) =
        resolveRepository().addMember(user, groupId)

    override suspend fun addGroup(accountingGroup: AccountingGroup, members: List<User>) =
        resolveRepository().addGroup(accountingGroup, members)

    private fun resolveRepository() = if (preferences.isSourceRemote()) {
        repositoryRemoteProvider
    } else {
        localDataProvider
    }
}
