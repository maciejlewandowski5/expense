package com.example.e.data.repository.remote

import com.example.e.data.*
import com.example.e.data.addObject
import com.example.e.data.fetchList
import com.example.e.data.remotemodels.*
import com.example.e.data.repository.Repository
import com.example.e.domain.*
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ExperimentalSerializationApi
class RemoteDataProvider @Inject constructor(
    private val groupService: GroupService,
    private val userService: UserService
) : Repository {

    override suspend fun allGroups() = fetchObject { groupService.allGroups() }

    override suspend fun groupDetails(groupId: GroupId) =
        fetchObject { groupService.groupDetails(groupId.value) }

    override suspend fun groupUsers(groupId: GroupId) =
        fetchList { userService.groupUsers(groupId.value) }

    override suspend fun addUser(user: User) =
        addObject { userService.addUser(AddUserRequest(user.name)) }

    override suspend fun allUsers() = fetchList { userService.allUsers() }

    override suspend fun addMember(user: User, groupId: GroupId) =
        addObject { groupService.addMember(UserRequest(user.id, user.name), groupId.value) }

    override suspend fun addExpense(expense: Expense, groupId: GroupId) = addObject {
        groupService.addExpense(
            ExpenseRequest(
                expense.id,
                expense.title,
                expense.participants.map {
                    ParticpantRequest(
                        it.id,
                        UserRequest(it.user.id, it.user.name),
                        amount = it.amount.toEngineeringString()
                    )
                },
                expense.date.format(DateTimeFormatter.ISO_DATE_TIME)
            ),
            groupId.value
        )
    }

    override suspend fun addGroup(
        accountingGroup: AccountingGroup,
        members: List<User>
    ) = addObject {
        groupService.addGroup(
            AddGroupRequest(
                AccountingGroupRequest(accountingGroup.id.value, accountingGroup.title),
                members.map { UserRequest(it.id, it.name) }
            )
        )
    }.map { GroupId(it) }
}
