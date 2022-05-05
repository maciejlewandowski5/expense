package com.example.e.domain

import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.changeSign
import com.example.e.data.MemberModel
import com.example.e.data.ParticipantModel
import com.example.e.data.UserModel
import com.example.e.domain.Expense.Companion.of
import com.example.e.domain.Participant.Companion.balance
import kotlinx.serialization.Serializable
import java.math.BigDecimal

data class Participant(
    val id: Long,
    val user: User,
    val amount: BigDecimal,
) : DomainModel {
    fun toParticipantModel(expenseId: Long): ParticipantModel =
        ParticipantModel(id = 0, userId = user.id, expenseId = expenseId, amount = amount)

    companion object {
        fun List<Participant>.balance(): BigDecimal =
            fold(BigDecimal.ZERO) { acc, participant -> acc.plus(participant.amount) }

        fun List<Participant>.getDebtors() = filter { it.amount > BigDecimal.ZERO }
            .associateBy({ it }, { it.amount })

        fun List<Participant>.getLoaners() = filter { it.amount < BigDecimal.ZERO }
            .associateBy({ it }, { it.amount.changeSign() })
    }
}

@Serializable
data class User(
    val id: String,
    val name: String
) : DomainModel {
    fun toUserModel() = UserModel(id = id, name = name)

    fun toMemberModel(groupId: Long) =
        MemberModel(id = 0, userId = id, accountingGroupId = groupId)

    companion object {
        fun List<User>.toParticipantCardState(selected: Boolean) =
            map { ParticpantCardState(it, selected) }

        fun List<User>.toParticipants(expenses: List<Expense>) = map { member ->
            Participant(
                id = 0,
                user = member,
                amount = expenses.of(member).balance().changeSign()
            )
        }
    }
}
