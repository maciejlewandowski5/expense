package com.example.e.data.remotemodels

data class ExpenseRequest(
    val id: Long,
    val title: String,
    val participants: List<ParticpantRequest>,
    val date: String // ISO_DATE_TIME_FORMAT
)

data class ParticpantRequest(
    val id: Long,
    val user: UserRequest,
    val amount: String // ENGINEERING STRING
)
