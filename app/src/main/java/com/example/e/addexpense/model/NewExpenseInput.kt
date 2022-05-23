package com.example.e.addexpense.model

import com.example.e.BIG_DECIMAL_SCALE
import com.example.e.DEFAULT_ID_FOR_AUTOGENERATE
import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.addexpense.participantpicker.ParticpantCardState.Companion.toParticipants
import com.example.e.changeSign
import com.example.e.domain.Expense
import com.example.e.domain.Participant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializer(forClass = LocalDateTime::class)
object DateSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DateSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_DATE_TIME)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        return encoder.encodeString(value.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}

@Serializer(forClass = BigDecimal::class)
object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BigDecimal {
        return BigDecimal(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        return encoder.encodeString(value.toEngineeringString())
    }
}

@Serializable()
data class NewExpenseInput(
    val borrowers: List<ParticpantCardState>,
    val payers: List<ParticpantCardState>,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val title: String,
    @Serializable(with = DateSerializer::class)
    val date: LocalDateTime,
    val isExternal: Boolean
) {
    fun toExpense() = Expense(
        id = DEFAULT_ID_FOR_AUTOGENERATE,
        title = title,
        participants = asParticipants(),
        date = date,
        isExternal = isExternal
    )

    private fun asParticipants() =
        borrowers.asParticipants(BigDecimal::changeSign) + payers.asParticipants()

    private fun List<ParticpantCardState>.asParticipants(additionalOperator: BigDecimal.() -> BigDecimal = { this }) =
        selected().toParticipants(amount.splitBetween(this).additionalOperator())

    private fun BigDecimal.splitBetween(participants: List<ParticpantCardState>): BigDecimal =
        divide(
            BigDecimal.valueOf(participants.selected().size.toDouble()),
            BIG_DECIMAL_SCALE,
            RoundingMode.HALF_UP
        )

    private fun List<ParticpantCardState>.selected() = filter { it.isSelected }
    fun toExpense(borrowers: List<Participant>, payers: List<Participant>) =
        Expense(
            id = DEFAULT_ID_FOR_AUTOGENERATE,
            title = title,
            participants = borrowers + payers,
            date = date,
            isExternal = isExternal
        )
}
