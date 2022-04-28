package com.example.e.data.repository.remote

import android.accounts.AuthenticatorException
import arrow.core.Validated
import arrow.core.invalid
import com.example.e.addObject
import com.example.e.fetchList
import com.example.e.data.remotemodels.*
import com.example.e.data.repository.Repository
import com.example.e.domain.*
import com.example.e.fetchObject
import com.example.e.login.session.TokenRepository
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ExperimentalSerializationApi
class RemoteDataProvider @Inject constructor(
    private val groupService: GroupService,
    private val userService: UserService,
    private val tokenRepository: TokenRepository
) : Repository {

    private fun isTokenNotNull() = tokenRepository.accessToken.value != null

    private suspend fun <T> callIfAuthExist(
        call: suspend () -> Validated<Throwable, T>
    ): Validated<Throwable, T> {
        return if (isTokenNotNull()) call() else AuthenticatorException().invalid()
    }

    override suspend fun allGroups() =
        callIfAuthExist { fetchObject { groupService.allGroups() } }

    override suspend fun groupDetails(groupId: GroupId) =
        callIfAuthExist { fetchObject { groupService.groupDetails(groupId.value) } }

    override suspend fun groupUsers(groupId: GroupId) =
        callIfAuthExist { fetchList { userService.groupUsers(groupId.value) } }

    override suspend fun addUser(user: User) =
        callIfAuthExist { addObject { userService.addUser(AddUserRequest(user.name)) } }

    override suspend fun allUsers() = callIfAuthExist { fetchList { userService.allUsers() } }

    override suspend fun addMember(user: User, groupId: GroupId) =
        callIfAuthExist {
            addObject {
                groupService.addMember(
                    UserRequest(user.id, user.name),
                    groupId.value
                )
            }
        }

    override suspend fun addExpense(expense: Expense, groupId: GroupId) = callIfAuthExist {
        addObject {
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
    }

    override suspend fun addGroup(
        accountingGroup: AccountingGroup,
        members: List<User>
    ) = callIfAuthExist {
        addObject {
            groupService.addGroup(
                AddGroupRequest(
                    AccountingGroupRequest(accountingGroup.id.value, accountingGroup.title),
                    members.map { UserRequest(it.id, it.name) }
                )
            )
        }.map { GroupId(it) }
    }
}
