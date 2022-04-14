package com.example.e.addexpense.participantpicker

import com.example.e.DEFAULT_ID_FOR_AUTOGENERATE
import com.example.e.domain.Participant
import com.example.e.domain.User
import java.math.BigDecimal

data class ParticpantCardState(
    val user: User,
    val isSelected: Boolean
) {
    companion object {
        fun List<ParticpantCardState>.toParticipants(amount: BigDecimal) = map {
            Participant(DEFAULT_ID_FOR_AUTOGENERATE, it.user, amount)
        }
    }
}
