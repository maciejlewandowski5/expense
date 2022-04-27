package com.example.e.data.remotemodels

import com.example.e.data.remotemodels.AccountingGroupResponse.Companion.toDomain
import com.example.e.data.remotemodels.ExpenseResponse.Companion.toDomain
import com.example.e.data.remotemodels.ParticipantResponse.Companion.toDomain
import com.example.e.data.remotemodels.UserResponse.Companion.toDomain
import com.example.e.domain.*
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class AllGroupsResponse(
    val groups: List<AccountingGroupResponse>
) : ResponseModel<AllGroups> {
    override fun toDomain(): AllGroups = AllGroups(groups = groups.toDomain())
}

@Serializable
data class AccountingGroupResponse(
    val id: Long,
    val title: String
) : ResponseModel<AccountingGroup> {
    companion object {
        fun List<AccountingGroupResponse>.toDomain() = map { it.toDomain() }
    }

    override fun toDomain(): AccountingGroup = AccountingGroup(id = GroupId(id), title = title)
}

@Serializable
data class GroupDetailsResponse(
    val accountingGroup: AccountingGroupResponse,
    val expenses: List<ExpenseResponse>,
    val members: List<UserResponse>
) : ResponseModel<GroupDetails> {
    override fun toDomain(): GroupDetails = GroupDetails(
        accountingGroup = accountingGroup.toDomain(),
        expenses = expenses.toDomain(),
        members = members.toDomain()
    )
}

@Serializable
data class ExpenseResponse(
    val id: Long,
    val title: String,
    val participants: List<ParticipantResponse>,
    val date: String
) : ResponseModel<Expense> {
    override fun toDomain() = Expense(
        id = id,
        title = title,
        participants = participants.toDomain(),
        date = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
    )

    companion object {
        fun List<ExpenseResponse>.toDomain() = map { it.toDomain() }
    }
}

@Serializable
data class UserResponse(
    val id: String,
    val name: String
) : ResponseModel<User> {
    override fun toDomain() = User(id = id, name = name)

    companion object {
        fun List<UserResponse>.toDomain() = map { it.toDomain() }
    }
}

@Serializable
data class ParticipantResponse(
    val id: Long,
    val user: UserResponse,
    val amount: String,
) {
    fun toDomain() = Participant(id = id, user = user.toDomain(), amount = BigDecimal(amount))

    companion object {
        fun List<ParticipantResponse>.toDomain() = map { it.toDomain() }
    }
}
