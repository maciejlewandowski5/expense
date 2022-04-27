package com.example.e.data.repository.local

import arrow.core.invalid
import arrow.core.valid
import com.example.e.DEFAULT_AUTOGENERATE_GROUP_ID
import com.example.e.data.*
import com.example.e.data.AccountingGroupModel.Companion.toDomain
import com.example.e.data.ParticipantModel.Companion.toDomain
import com.example.e.data.UserModel.Companion.toDomain
import com.example.e.data.repository.Repository
import com.example.e.domain.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.IllegalArgumentException
import java.time.Clock
import javax.inject.Inject

class LocalDataProvider @Inject constructor(
    private val groupDao: GroupDao,
    private val userDao: UserDao,
    private val participantDao: ParticipantDao,
    private val expenseDao: ExpenseDao,
    private val memberDao: MemberDao,
    private val clock: Clock
) : Repository {

    private suspend fun <G> catchExceptionToEither(accessDatabase: suspend () -> G) =
        try {
            accessDatabase.invoke().valid()
        } catch (e: Throwable) {
            e.invalid()
        }

    override suspend fun allGroups() = catchExceptionToEither { groupDao.groups().toDomain() }

    override suspend fun groupDetails(groupId: GroupId) = coroutineScope {
        catchExceptionToEither {
            val usersAsync = async { groupUsers(groupId) }
            groupDao.groupWithExpenses(groupId.value)
                .let {
                    it.toGroupDetails(
                        usersAsync.await().fold({ throw IllegalArgumentException() }, { it })
                    )
                }
        }
    }

    private suspend fun AccountingGroupWithExpensesModel?.toGroupDetails(users: List<User>) =
        GroupDetails(
            accountingGroup = this?.accountingGroupModel?.toDomain() ?: AccountingGroup(
                DEFAULT_AUTOGENERATE_GROUP_ID,
                DEFAULT_GROUP_NAME
            ),
            expenses = participantsForExpenses(this, users),
            members = users
        )

    private suspend fun participantsForExpenses(
        it: AccountingGroupWithExpensesModel?,
        users: List<User>
    ) = it?.expenses?.map {
        it.toDomain(participantDao.participants(it.id).toDomain(users), clock)
    } ?: emptyList()

    override suspend fun groupUsers(groupId: GroupId) = catchExceptionToEither {
        userDao.groupUsers(groupId.value).toDomain()
    }

    override suspend fun addExpense(expense: Expense, groupId: GroupId) = catchExceptionToEither {
        expenseDao.insertExpenseAndParticipants(
            expense.toExpenseModel(groupId),
            expense.participants
        )
    }

    override suspend fun addUser(user: User) =
        catchExceptionToEither { userDao.insert(user.toUserModel()) }

    override suspend fun allUsers() = catchExceptionToEither { userDao.users().toDomain() }

    override suspend fun addMember(user: User, groupId: GroupId) = catchExceptionToEither {
        memberDao.insert(user.toMemberModel(groupId.value))
    }

    override suspend fun addGroup(accountingGroup: AccountingGroup, members: List<User>) =
        catchExceptionToEither {
            GroupId(groupDao.addGroupWithMembers(accountingGroup, members))
        }

    companion object {
        private const val DEFAULT_GROUP_NAME = "Example group"
    }
}
