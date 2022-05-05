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
    private val remoteDataProvider: RemoteDataProvider,
    private val preferences: Preferences
) : Repository {

    private val repositoryResolver: Map<Boolean, Repository> = mapOf(
        true to remoteDataProvider,
        false to localDataProvider
    )

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

    override suspend fun deleteExpense(expense: Expense, groupId: GroupId) {
        resolveRepository().deleteExpense(expense, groupId)
    }

    private fun resolveRepository() =
        repositoryResolver.getOrDefault(preferences.isSourceRemote(), localDataProvider)
}
