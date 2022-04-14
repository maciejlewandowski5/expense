package com.example.e.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.e.data.ParticipantModel.Companion.toDomain
import com.example.e.domain.*
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime

@Entity(tableName = "expenses_table")
data class ExpenseModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val expanseDate: LocalDateTime?,
    val accountingGroupId: Long
) {
    fun toDomain(participants: List<Participant>, clock: Clock) = Expense(
        id = id,
        title = title,
        participants = participants,
        date = expanseDate ?: LocalDateTime.now(clock)
    )
}

@Entity(tableName = "users_table")
data class UserModel(
    @PrimaryKey()
    val id: String,
    val name: String
) {
    fun toDomain() = User(id = id, name = name)

    companion object {
        fun List<UserModel>.toDomain() = map { it.toDomain() }
    }
}

@Entity(tableName = "groups_table")
data class AccountingGroupModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
) {
    fun toDomain() = AccountingGroup(id = GroupId(id), title = title)

    companion object {
        fun List<AccountingGroupModel>.toDomain() = map { it.toDomain() }
    }
}

@Entity(tableName = "participants_table")
data class ParticipantModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val userId: String,
    val amount: BigDecimal,
    val expenseId: Long
) {
    fun toDomain(accountGroupMembers: List<User>) =
        Participant(id, user = accountGroupMembers.first { it.id == userId }, amount = amount)

    companion object {
        fun List<ParticipantModel>.toDomain(accountGroupMembers: List<User>) =
            map { it.toDomain(accountGroupMembers) }
    }
}

@Entity(tableName = "members_table")
data class MemberModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val userId: String,
    val accountingGroupId: Long
)

data class ExpenseWithParticipantsModel(
    @Embedded val expenseModel: ExpenseModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "expenseId"
    )
    val participantModel: List<ParticipantModel>
)

data class AccountingGroupWithExpensesModel(
    @Embedded val accountingGroupModel: AccountingGroupModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountingGroupId"
    )
    val expenses: List<ExpenseModel>
)
