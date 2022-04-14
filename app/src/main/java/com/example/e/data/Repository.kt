package com.example.e.data

import com.example.e.DEFAULT_AUTOGENERATE_GROUP_ID
import com.example.e.data.ParticipantModel.Companion.toDomain
import com.example.e.data.UserModel.Companion.toDomain
import com.example.e.domain.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

class Repository @Inject constructor(
    private val groupDao: GroupDao,
    private val userDao: UserDao,
    private val participantDao: ParticipantDao,
    private val expenseDao: ExpenseDao,
    private val memberDao: MemberDao,
    private val clock: Clock
) {

    suspend fun allGroups() = groupDao.groups().map { it.toDomain() }

    suspend fun groupDetails(groupId: GroupId): GroupDetails = coroutineScope {
        val usersAsync = async { groupUsers(groupId) }
        groupDao.groupWithExpenses(groupId.value).let { it.toGroupDetails(usersAsync.await()) }
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

    suspend fun groupUsers(groupId: GroupId) = userDao.groupUsers(groupId.value).toDomain()

    companion object {
        private const val DEFAULT_GROUP_NAME = "Example group"
    }

    suspend fun dummyData() {
        groupDao.insert(AccountingGroupModel(1, "Przykład0"))
        groupDao.insert(AccountingGroupModel(2, "Przykład1"))
        groupDao.insert(AccountingGroupModel(3, "Przykład2"))
        groupDao.insert(AccountingGroupModel(4, "Przykład3"))
        groupDao.insert(AccountingGroupModel(5, "Przykład4"))

        expenseDao.insert(ExpenseModel(1, "title0", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(2, "title1", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(3, "title2", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(4, "title3", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(5, "title4", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(6, "title5", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(7, "title6", LocalDateTime.now(), 1))

        expenseDao.insert(ExpenseModel(15, "title15", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(16, "title16", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(17, "title17", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(18, "title18", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(19, "title19", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(20, "title20", LocalDateTime.now(), 1))
        expenseDao.insert(ExpenseModel(21, "title21", LocalDateTime.now(), 1))

        expenseDao.insert(ExpenseModel(8, "title0", LocalDateTime.now(), 2))
        expenseDao.insert(ExpenseModel(9, "title1", LocalDateTime.now(), 2))
        expenseDao.insert(ExpenseModel(10, "title2", LocalDateTime.now(), 2))
        expenseDao.insert(ExpenseModel(11, "title3", LocalDateTime.now(), 3))
        expenseDao.insert(ExpenseModel(12, "title4", LocalDateTime.now(), 3))
        expenseDao.insert(ExpenseModel(13, "title5", LocalDateTime.now(), 3))
        expenseDao.insert(ExpenseModel(14, "title6", LocalDateTime.now(), 4))

        participantDao.insert(ParticipantModel(1, "1", BigDecimal.TEN, 1))
        participantDao.insert(ParticipantModel(2, "2", BigDecimal("-10"), 1))
        participantDao.insert(ParticipantModel(3, "1", BigDecimal.TEN, 2))
        participantDao.insert(ParticipantModel(4, "2", BigDecimal("-10"), 2))
        participantDao.insert(ParticipantModel(5, "1", BigDecimal.ONE, 3))
        participantDao.insert(ParticipantModel(6, "2", BigDecimal("-1"), 3))
        participantDao.insert(ParticipantModel(7, "1", BigDecimal.ONE, 4))
        participantDao.insert(ParticipantModel(8, "2", BigDecimal("-1"), 4))

        userDao.insert(UserModel("1", "Ala"))
        userDao.insert(UserModel("2", "Jan"))

        memberDao.insert(MemberModel(1, "1", 1))
        memberDao.insert(MemberModel(2, "2", 1))
        memberDao.insert(MemberModel(3, "1", 2))
        memberDao.insert(MemberModel(4, "2", 2))
        memberDao.insert(MemberModel(5, "1", 3))
        memberDao.insert(MemberModel(6, "2", 3))
    }

    suspend fun addExpense(expense: Expense, groupId: GroupId) {
        expenseDao.insertExpenseAndParticipants(
            expense.toExpenseModel(groupId),
            expense.participants
        )
    }

    suspend fun addUser(user: User) = userDao.insert(user.toUserModel())

    suspend fun allUsers(): List<User> = userDao.users().toDomain()

    suspend fun addMember(user: User, groupId: GroupId) =
        memberDao.insert(user.toMemberModel(groupId.value))

    suspend fun addGroup(accountingGroup: AccountingGroup, members: List<User>) =
        GroupId(groupDao.addGroupWithMembers(accountingGroup, members))
}
