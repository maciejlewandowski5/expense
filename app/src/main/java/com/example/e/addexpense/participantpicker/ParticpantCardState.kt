package com.example.e.addexpense.participantpicker

import com.example.e.DEFAULT_ID_FOR_AUTOGENERATE
import com.example.e.domain.Participant
import com.example.e.domain.User
import com.example.e.split.CustomSplitParticipant
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class ParticpantCardState(
    val user: User,
    val isSelected: Boolean
) {
    companion object {
        fun List<ParticpantCardState>.toParticipants(amount: BigDecimal) = map {
            Participant(DEFAULT_ID_FOR_AUTOGENERATE, it.user, amount)
        }

        fun List<ParticpantCardState>.toCustomSplitParticipants() =
            toParticipants(
                BigDecimal.ZERO
            ).map {
                CustomSplitParticipant(
                    participant = it,
                    amountInput = ""
                )
            }
    }
}
